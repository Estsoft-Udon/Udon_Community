package com.example.estsoft_udon_community.util;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import com.example.estsoft_udon_community.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ModelUtil {
    private final LocationService locationService;

    public void setCategories(Model model) {
        model.addAttribute("articleCategories", ArticleCategory.values());
    }

    public void setLocations(Model model) {
        // 상위 지역 목록을 가져와서 모델에 추가
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 첫 번째 상위 지역에 대한 하위 지역 목록을 초기화하여 모델에 추가
        List<Location> lowerLocations = new ArrayList<>();
        if (!upperLocations.isEmpty()) {
            for (String upperLocation : upperLocations) {
                List<Location> newLowerLocations = locationService.getLowerLocations(upperLocation);
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
