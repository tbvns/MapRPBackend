package xyz.tbvns.rpmap.DTO.Packets;

import com.fasterxml.jackson.annotation.JsonTypeName;
import xyz.tbvns.rpmap.DTO.Features.GeoJsonFeature;

@JsonTypeName("modify") // Links this class to the 'modify' type value
public class ModifyPacket extends MapUpdatePacket {
    private String id;
    private GeoJsonFeature data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoJsonFeature getData() {
        return data;
    }

    public void setData(GeoJsonFeature data) {
        this.data = data;
    }

     @Override
    public String getType() {
        return "modify";
    }
}