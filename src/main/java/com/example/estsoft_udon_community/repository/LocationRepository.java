package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.enums.UpperLocationEnum;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT DISTINCT upperLocation FROM Location")
    List<String> findDistinctUpperLocations();

    List<Location> findLowerLocationsByUpperLocation(UpperLocationEnum upperLocation);

    Optional<Location> findByName(String name);
}

