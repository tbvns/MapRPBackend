package xyz.tbvns.rpmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import xyz.tbvns.rpmap.DTO.Packets.MapUpdatePacket;
import xyz.tbvns.rpmap.DataManager;

import java.util.Optional;

@Controller
public class MapUpdateController {

    private static final Logger logger = LoggerFactory.getLogger(MapUpdateController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final DataManager dataManager;

    @Autowired
    public MapUpdateController(SimpMessagingTemplate messagingTemplate, DataManager dataManager) {
        this.messagingTemplate = messagingTemplate;
        this.dataManager = dataManager;
    }

    @MessageMapping("/mapUpdate")
    public void receiveMapUpdate(MapUpdatePacket packet, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        if (packet == null) {
            logger.warn("Received null packet from session ID: {}", sessionId);
            return;
        }
        logger.info("Controller received packet of type: '{}' from session ID: {}", packet.getType(), sessionId);

        Optional<MapUpdatePacket> processedPacketOpt = dataManager.processPacket(packet);

        if (processedPacketOpt.isPresent()) {
            MapUpdatePacket processedPacket = processedPacketOpt.get();
            messagingTemplate.convertAndSend("/topic/mapUpdate", processedPacket);
            logger.info("Successfully processed and broadcasted '{}' packet to /topic/mapUpdate.", processedPacket.getType());
        } else {
            logger.warn("Packet processing for type '{}' from session {} did not result in a broadcastable event. The packet might have been invalid or the operation failed.",
                    packet.getType(), sessionId);
        }
    }
}