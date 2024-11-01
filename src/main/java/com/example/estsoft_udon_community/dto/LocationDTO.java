package com.example.estsoft_udon_community.dto;

import com.example.estsoft_udon_community.entity.Location;
import lombok.Data;

@Data
public class LocationDTO {
    private Long id;
    private String name;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.name = location.getName();
    }
}
