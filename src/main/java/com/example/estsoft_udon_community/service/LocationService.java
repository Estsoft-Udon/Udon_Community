package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import com.example.estsoft_udon_community.repository.LocationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;


    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // LocationId로 Location 정보 가져오기
    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow();
    }

    public List<String> getDistinctUpperLocations() {
        return locationRepository.findDistinctUpperLocations();
    }

    public List<Location> getLowerLocation(String locationName) {
        UpperLocationEnum upperLocationEnum = UpperLocationEnum.fromString(locationName);
        return locationRepository.findByUpperLocation(upperLocationEnum);
    }

    // UpperLocation과 name을 통해 Location id를 리턴하는 메서드
    public Long getLocationIdByUpperLocationAndName(String upperLocation, String name) {
        UpperLocationEnum upperLocationEnum = UpperLocationEnum.fromString(upperLocation);
        Location location = locationRepository.findByUpperLocationAndName(upperLocationEnum, name);

        return location.getId();
    }
}
