package com.example.estsoft_udon_community.external.service;

import com.example.estsoft_udon_community.external.entity.FestivalDatas;
import com.example.estsoft_udon_community.external.repository.FestivalDatasRepository;
import com.example.estsoft_udon_community.external.dto.request.FestivalDatasRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalDatasService {
    private final FestivalDatasRepository repository;

    public List<FestivalDatas> getFestivalsByStartDate(String date) {

        return repository.findAllByStartDate(date);
    }

    public void setFesitvalDatas(String eventStartDate) throws Exception {
        if(!repository.existsByStartDate(eventStartDate)) {
            List<FestivalDatasRequest> datas = parseJsonToApiDTOList(fetchDatasFromExternalApi(eventStartDate));

            for (FestivalDatasRequest festivalDatasRequest : datas) {
                if(festivalDatasRequest.getStartDate().equals(eventStartDate)) {
                    FestivalDatas festivalData = new FestivalDatas(festivalDatasRequest);
                    repository.save(festivalData);
                }
            }
        }
    }

    private String fetchDatasFromExternalApi(String eventStartDate) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
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
    }

    public List<FestivalDatasRequest> parseJsonToApiDTOList(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 파싱을 위한 클래스를 정의한 것처럼, 맨 위 JSON에서 "response" -> "body" -> "items" -> "item" 데이터를 추출
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        // item 배열을 ApiDTO 리스트로 변환
        List<FestivalDatasRequest> festivalDatasRequestList = objectMapper.convertValue(itemsNode, objectMapper.getTypeFactory().constructCollectionType(List.class, FestivalDatasRequest.class));

        return festivalDatasRequestList;
    }
}