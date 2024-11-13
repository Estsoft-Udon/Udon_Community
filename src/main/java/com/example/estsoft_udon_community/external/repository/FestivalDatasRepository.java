package com.example.estsoft_udon_community.external.repository;

import com.example.estsoft_udon_community.external.entity.FestivalDatas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FestivalDatasRepository extends JpaRepository<FestivalDatas, Long> {
    boolean existsByStartDate(String startDate);
    List<FestivalDatas> findAllByStartDate(String startDate);
}
