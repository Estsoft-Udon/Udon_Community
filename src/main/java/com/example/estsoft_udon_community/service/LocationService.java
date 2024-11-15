package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import com.example.estsoft_udon_community.repository.LocationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;


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

    public void setCategories(Model model) {
        model.addAttribute("articleCategories", ArticleCategory.values());
    }

    public void setLocations(Model model) {
        // 상위 지역 목록을 가져와서 모델에 추가
        List<String> upperLocations = getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
        List<Location> lowerLocations = new ArrayList<>();
        if (!upperLocations.isEmpty()) {
            for (String upperLocation : upperLocations) {
                List<Location> newLowerLocations = getLowerLocations(upperLocation);
                lowerLocations.addAll(newLowerLocations);
            }
        }
        model.addAttribute("locations", lowerLocations);
    }

    public void setCategoriesAndLocations(Model model) {
        setCategories(model);
        setLocations(model);
    }
}
