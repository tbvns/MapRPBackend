package xyz.tbvns.rpmap.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.tbvns.rpmap.DTO.Packets.BulkAddPacket;
import xyz.tbvns.rpmap.DataManager;

@RestController
public class InitialDataController {

    private final DataManager dataManager;

    @Autowired
    public InitialDataController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @CrossOrigin(value = "*")
    @GetMapping("/getMaps")
    public BulkAddPacket getInitialMapData() {
        BulkAddPacket packet = new BulkAddPacket();
        packet.setData(dataManager.getAllFeatures());
        return packet;
    }
}