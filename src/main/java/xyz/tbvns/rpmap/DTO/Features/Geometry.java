package xyz.tbvns.rpmap.DTO.Features;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Point.class, name = "Point"),
    @JsonSubTypes.Type(value = LineString.class, name = "LineString"),
    @JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
    // Add Circle here if you decide to send it as a distinct Geometry type
    // Based on your JS, 'Circle' exists, but might be Point + radius property.
    // For now, let's assume Point/LineString/Polygon are the main geometry types.
    // We'll handle 'Circle' as a Point with radius in properties, as it's safer.
})
public abstract class Geometry {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}