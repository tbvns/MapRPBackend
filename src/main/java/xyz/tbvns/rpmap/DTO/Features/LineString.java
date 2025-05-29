package xyz.tbvns.rpmap.DTO.Features;

import java.util.List;

public class LineString extends Geometry {
    private List<List<Double>> coordinates; // [[lon1, lat1], [lon2, lat2], ...]

    public LineString() {
        setType("LineString");
    }

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }
}