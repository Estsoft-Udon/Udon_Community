package com.example.estsoft_udon_community.external.service;

import com.example.estsoft_udon_community.external.entity.FestivalDatas;
import com.example.estsoft_udon_community.external.repository.FestivalDatasRepository;
import com.example.estsoft_udon_community.external.dto.request.FestivalDatasRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalDatasService {
    private final RestTemplate restTemplate;
    private final FestivalDatasRepository repository;

    public List<FestivalDatas> getFestivalsByStartDate(String date) {

        return repository.findAllByStartDate(date);
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduledFetchData() throws Exception {
        LocalDate today = LocalDate.now();

        // 일주일 후의 날짜 계산
        LocalDate oneWeekLater = today.plusWeeks(1);

        // DateTimeFormatter를 사용하여 LocalDate를 String으로 변환
        String formattedDate = oneWeekLater.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        setFesitvalDatas(formattedDate);
    }

    public void setFesitvalDatas(String eventStartDate) throws Exception {
        if (!repository.existsByStartDate(eventStartDate)) {
            List<FestivalDatasRequest> datas = parseJsonToApiDTOList(fetchDatasFromExternalApi(eventStartDate));

            for (FestivalDatasRequest festivalDatasRequest : datas) {
                if (festivalDatasRequest.getStartDate().equals(eventStartDate)) {
                    FestivalDatas festivalData = new FestivalDatas(festivalDatasRequest);
                    repository.save(festivalData);
                }
            }
        }
    }

    private String fetchDatasFromExternalApi(String eventStartDate) throws URISyntaxException {
        try {
            String API_KEY = "ZYDEZVuyAQ03fcWtyacCzvJStZ8kJtlvSfJnWiT40dE8cbg4skyhHxgdd1VY0jtmdiaxPh07B8yUL8vQB8BhJA==";
            String url = "http://apis.data.go.kr/B551011/KorService1/searchFestival1" +
                    "?MobileOS=ETC&" +
                    "MobileApp=check&" +
                    "_type=json&" +
                    "eventStartDate=" + eventStartDate +
                    "&numOfRows=100" +
                    "&serviceKey=" + API_KEY;

            URI uri = new URI(url);

            // API 호출하여 JSON 응답 받기
            ResponseEntity<String> response = restTemplate.exchange(
                    uri, HttpMethod.GET, null, String.class);

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FestivalDatasRequest> parseJsonToApiDTOList(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        if (json == null) {
            return new ArrayList<FestivalDatasRequest>();
        }
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        // item 배열을 ApiDTO 리스트로 변환
        List<FestivalDatasRequest> festivalDatasRequestList = objectMapper.convertValue(itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, FestivalDatasRequest.class));

        return festivalDatasRequestList;
    }
}