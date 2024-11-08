package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import com.example.estsoft_udon_community.repository.LocationRepository;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;


    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // locationId -> Location
    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("위치 정보가 없습니다."));
    }

    // Distinct UpperLocation 정보
    public List<String> getDistinctUpperLocations() {
        return locationRepository.findDistinctUpperLocations();
    }

    // upperLocation 에 해당하는 LowerLocation 의 List<Location> return
    public List<Location> getLowerLocations(String upperLocation) {
        UpperLocationEnum upperLocationEnum = UpperLocationEnum.fromString(upperLocation);
        return locationRepository.findLowerLocationsByUpperLocation(upperLocationEnum);
    }

    public Location findByName(String name) {
        Location location = locationRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("No Found Location By Name"));

        return location;
    }

    public Location findByNameAndUpperLocation(String name, UpperLocationEnum upperLocation) {
        return locationRepository.findByNameAndUpperLocation(name, upperLocation)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
    }
}
