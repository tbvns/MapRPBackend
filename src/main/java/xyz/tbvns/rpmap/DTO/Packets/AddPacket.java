package xyz.tbvns.rpmap.DTO.Packets;

import com.fasterxml.jackson.annotation.JsonTypeName;
import xyz.tbvns.rpmap.DTO.Features.GeoJsonFeature;

@JsonTypeName("add") // Links this class to the 'add' type value
public class AddPacket extends MapUpdatePacket {
    private GeoJsonFeature data;

    public GeoJsonFeature getData() {
        return data;
    }

    public void setData(GeoJsonFeature data) {
        this.data = data;
    }

    @Override
    public String getType() {
        return "add";
    }
}