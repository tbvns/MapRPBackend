package xyz.tbvns.rpmap.DTO.Packets;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("remove") // Links this class to the 'remove' type value
public class RemovePacket extends MapUpdatePacket {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return "remove";
    }
}