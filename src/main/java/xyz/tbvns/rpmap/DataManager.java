package xyz.tbvns.rpmap;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service; // Or @Component
import xyz.tbvns.Config;
import xyz.tbvns.EZConfig;
import xyz.tbvns.rpmap.Configs.Save;
import xyz.tbvns.rpmap.DTO.Features.GeoJsonFeature;
import xyz.tbvns.rpmap.DTO.Packets.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages the state of GeoJSON features for the map.
 * This class is designed to be thread-safe for use in a concurrent environment.
 * It can be registered as a Spring service/component.
 */
@Service // Marks this class as a Spring service
public class DataManager {

    private static final Logger logger = LoggerFactory.getLogger(DataManager.class);

    // Using ConcurrentHashMap for thread-safe storage of features.
    // The key is the feature ID (String), and the value is the GeoJsonFeature object.
    private final Map<String, GeoJsonFeature> features = new ConcurrentHashMap<>();

    public DataManager() {
        features.putAll(Save.save);
    }

    /**
     * Processes an incoming MapUpdatePacket and applies the corresponding change
     * to the stored features.
     *
     * @param packet The packet to process (e.g., AddPacket, ModifyPacket).
     * @return An Optional containing the processed packet if the operation was successful
     * and should be broadcast, or an empty Optional if the operation failed
     * or no broadcast is necessary (e.g., trying to modify a non-existent feature).
     */
    @SneakyThrows
    public Optional<MapUpdatePacket> processPacket(MapUpdatePacket packet) {
        if (packet == null) {
            logger.warn("Received null packet for processing.");
            return Optional.empty();
        }

        logger.info("Processing packet of type: {}", packet.getType());

        boolean success = false;
        if (packet instanceof AddPacket) {
            success = addFeature(((AddPacket) packet).getData());
        } else if (packet instanceof ModifyPacket) {
            ModifyPacket modifyPacket = (ModifyPacket) packet;
            success = modifyFeature(modifyPacket.getId(), modifyPacket.getData());
        } else if (packet instanceof RemovePacket) {
            success = removeFeature(((RemovePacket) packet).getId());
        } else if (packet instanceof BulkAddPacket) {
            success = bulkAddFeatures(((BulkAddPacket) packet).getData());
        } else {
            logger.warn("Received unknown packet type: {}", packet.getClass().getName());
            return Optional.empty(); // Unknown packet type
        }

        Save.save = features;
        EZConfig.save();

        return success ? Optional.of(packet) : Optional.empty();
    }

    /**
     * Adds a single GeoJSON feature to the store.
     * If a feature with the same ID already exists, it will be overwritten.
     *
     * @param feature The GeoJsonFeature to add.
     * @return true if the feature was added or updated, false if the input feature or its ID was null.
     */
    public boolean addFeature(GeoJsonFeature feature) {
        if (feature == null || feature.getId() == null) {
            logger.warn("Attempted to add a null feature or feature with null ID.");
            return false;
        }
        features.put(feature.getId(), feature);
        logger.debug("Added/Updated feature with ID: {}", feature.getId());
        return true;
    }

    /**
     * Modifies an existing GeoJSON feature.
     * The feature is identified by its ID. If no feature with the given ID exists,
     * the operation will not be performed.
     *
     * @param id      The ID of the feature to modify.
     * @param feature The GeoJsonFeature containing the updated data.
     * The ID within this feature object should match the 'id' parameter.
     * @return true if the feature was successfully modified, false otherwise (e.g., feature not found, ID mismatch).
     */
    public boolean modifyFeature(String id, GeoJsonFeature feature) {
        if (id == null || feature == null || feature.getId() == null) {
            logger.warn("Attempted to modify feature with null ID or null feature data.");
            return false;
        }
        if (!id.equals(feature.getId())) {
            logger.warn("Mismatched IDs during feature modification. Parameter ID: {}, Feature Data ID: {}", id, feature.getId());
            // Optionally, you could force the feature's ID to match the parameter 'id'
            // feature.setId(id);
            // For now, we consider it an error.
            return false;
        }
        if (features.containsKey(id)) {
            features.put(id, feature); // Overwrites the existing feature
            logger.debug("Modified feature with ID: {}", id);
            return true;
        } else {
            logger.warn("Attempted to modify non-existent feature with ID: {}", id);
            return false; // Feature not found
        }
    }

    /**
     * Removes a GeoJSON feature from the store by its ID.
     *
     * @param id The ID of the feature to remove.
     * @return true if the feature was successfully removed, false if no feature with the given ID was found or ID was null.
     */
    public boolean removeFeature(String id) {
        if (id == null) {
            logger.warn("Attempted to remove feature with null ID.");
            return false;
        }
        GeoJsonFeature removedFeature = features.remove(id);
        if (removedFeature != null) {
            logger.debug("Removed feature with ID: {}", id);
            return true;
        } else {
            logger.warn("Attempted to remove non-existent feature with ID: {}", id);
            return false; // Feature not found
        }
    }

    /**
     * Adds a list of GeoJSON features to the store.
     * This is more efficient than adding them one by one if you have many features.
     * Existing features with the same IDs will be overwritten.
     *
     * @param featureList A list of GeoJsonFeature objects to add.
     * @return true if all valid features in the list were processed, false if the list was null or empty.
     * Note: Individual null features or features with null IDs within the list will be skipped.
     */
    public boolean bulkAddFeatures(List<GeoJsonFeature> featureList) {
        if (featureList == null || featureList.isEmpty()) {
            logger.warn("Attempted to bulk add a null or empty list of features.");
            return false;
        }
        int addedCount = 0;
        for (GeoJsonFeature feature : featureList) {
            if (feature != null && feature.getId() != null) {
                features.put(feature.getId(), feature);
                addedCount++;
            } else {
                logger.warn("Skipped a null feature or feature with null ID during bulk add.");
            }
        }
        logger.debug("Bulk added {} features.", addedCount);
        return addedCount > 0 || !featureList.isEmpty(); // Return true if any were processed or list wasn't empty initially
    }

    /**
     * Retrieves a specific feature by its ID.
     *
     * @param id The ID of the feature to retrieve.
     * @return An Optional containing the GeoJsonFeature if found, otherwise an empty Optional.
     */
    public Optional<GeoJsonFeature> getFeatureById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(features.get(id));
    }

    /**
     * Retrieves all currently stored GeoJSON features as a list.
     * The list is a copy, so modifications to it will not affect the internal store.
     *
     * @return A new List containing all GeoJsonFeature objects. Returns an empty list if no features are stored.
     */
    public List<GeoJsonFeature> getAllFeatures() {
        if (features.isEmpty()) {
            return Collections.emptyList();
        }
        // Create a new list from the values of the map to prevent external modification
        return new ArrayList<>(features.values());
    }

    /**
     * Clears all features from the store.
     * Useful for resetting the state.
     */
    public void clearAllFeatures() {
        features.clear();
        logger.info("All features have been cleared from the DataManager.");
    }

    /**
     * Gets the current number of features stored.
     * @return The count of features.
     */
    public int getFeatureCount() {
        return features.size();
    }
}
