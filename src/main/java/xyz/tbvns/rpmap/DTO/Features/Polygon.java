package xyz.tbvns.rpmap.DTO.Features;

import java.util.List;

public class Polygon extends Geometry {
    private List<List<List<Double>>> coordinates; // [[[lon1, lat1], ...], [[hole1_lon1, hole1_lat1], ...]]

    public Polygon() {
        setType("Polygon");
    }

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}