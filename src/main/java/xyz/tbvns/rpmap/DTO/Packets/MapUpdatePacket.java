package xyz.tbvns.rpmap.DTO.Packets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// This tells Jackson to use the 'type' field to decide which subclass to create
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AddPacket.class, name = "add"),
    @JsonSubTypes.Type(value = ModifyPacket.class, name = "modify"),
    @JsonSubTypes.Type(value = RemovePacket.class, name = "remove"),
    @JsonSubTypes.Type(value = BulkAddPacket.class, name = "bulkAdd")
})
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore fields not defined here
public abstract class MapUpdatePacket {
    // No common fields needed for now, but serves as the base for polymorphism
    // We need a 'type' field, but it's handled by @JsonTypeInfo during serialization/deserialization.
    // However, it's good practice to have it in subclasses for clarity if needed.
    public abstract String getType();
}