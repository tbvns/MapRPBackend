package xyz.tbvns.rpmap.DTO.Packets;

import com.fasterxml.jackson.annotation.JsonTypeName;
import xyz.tbvns.rpmap.DTO.Features.GeoJsonFeature;

import java.util.List;

@JsonTypeName("bulkAdd") // Links this class to the 'bulkAdd' type value
public class BulkAddPacket extends MapUpdatePacket {
    private List<GeoJsonFeature> data;

    public List<GeoJsonFeature> getData() {
        return data;
    }

    public void setData(List<GeoJsonFeature> data) {
        this.data = data;
    }

    @Override
    public String getType() {
        return "bulkAdd";
    }
}