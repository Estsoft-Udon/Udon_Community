package com.example.estsoft_udon_community.external.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FestivalDatasRequest {

    @JsonProperty("eventstartdate")
    private String startDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("addr1")
    private String content;
    @JsonProperty("eventenddate")
    private String endDate;
}
