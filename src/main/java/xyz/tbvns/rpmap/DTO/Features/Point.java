package xyz.tbvns.rpmap.DTO.Features;

import java.util.List;

public class Point extends Geometry {
    private List<Double> coordinates; // [lon, lat]

    public Point() {
        setType("Point");
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}