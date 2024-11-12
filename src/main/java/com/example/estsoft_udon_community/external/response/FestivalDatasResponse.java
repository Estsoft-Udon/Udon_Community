package com.example.estsoft_udon_community.external.response;

import com.example.estsoft_udon_community.external.entity.FestivalDatas;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FestivalDatasResponse {
    private String startDate;
    private String title;
    private String content;
    private String endDate;

    public FestivalDatasResponse(FestivalDatas data) {
        this.startDate = data.getStartDate();
        this.title = data.getTitle();
        this.content = data.getContent();
        this.endDate = data.getEndDate();
    }
}
