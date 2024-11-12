package com.example.estsoft_udon_community.external.entity;

import com.example.estsoft_udon_community.external.dto.request.FestivalDatasRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class FestivalDatas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    public FestivalDatas(FestivalDatasRequest festivalDatasRequest) {
        this.title = festivalDatasRequest.getTitle();
        this.content = festivalDatasRequest.getContent();
        this.startDate = festivalDatasRequest.getStartDate();
        this.endDate = festivalDatasRequest.getEndDate();
    }
}
