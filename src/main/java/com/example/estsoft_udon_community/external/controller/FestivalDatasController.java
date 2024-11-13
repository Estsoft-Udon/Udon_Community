package com.example.estsoft_udon_community.external.controller;

import com.example.estsoft_udon_community.external.dto.response.FestivalDatasResponse;
import com.example.estsoft_udon_community.external.entity.FestivalDatas;
import com.example.estsoft_udon_community.external.service.FestivalDatasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FestivalDatasController {
    private final FestivalDatasService festivalDatasService;
    private final RestTemplate restTemplate;

    @GetMapping("/api/festival")
    public ResponseEntity<List<FestivalDatasResponse>> getExternalFestivalDatas(@RequestParam String date) throws Exception {
        List<FestivalDatas> datas = festivalDatasService.getFestivalsByStartDate(date);

        List<FestivalDatasResponse> festivalDatasResponse = datas.stream()
                .map(FestivalDatasResponse::new)  // FestivalDatas를 ApiResponse로 변환
                .toList();  // 리스트로 수집

        // apiDTOList를 HTTP 응답 본문으로 반환
        return ResponseEntity.ok(festivalDatasResponse);
    }
}

