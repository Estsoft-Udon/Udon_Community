package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT DISTINCT upperLocation FROM Location")
    List<String> findDistinctUpperLocations();

    List<Location> findByUpperLocation(UpperLocationEnum upperLocation);

    // UpperLocation과 name을 통해 Location을 찾는 메서드
    Location findByUpperLocationAndName(UpperLocationEnum upperLocation, String name);

}

