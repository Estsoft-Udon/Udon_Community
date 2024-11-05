package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    // 인증이 안되니까 하위 선택이 안들어왔다.
    @GetMapping("/getLowerLocations")
    public List<Location> getLowerLocations(@RequestParam String upperLocation) {
        return locationService.getLowerLocation(upperLocation);
    }
}
