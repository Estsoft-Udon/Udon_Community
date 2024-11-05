package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.PasswordHint;
import com.example.estsoft_udon_community.security.CustomUserDetails;
import com.example.estsoft_udon_community.service.LocationService;
import com.example.estsoft_udon_community.security.UsersDetailService;
import com.example.estsoft_udon_community.service.UsersService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UsersService usersService;
    private final LocationService locationService;
    private final UsersDetailService usersDetailService;

    private Long userId;


    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/find_id")
    public String findId(Model model) {
        model.addAttribute("isIdFound", false);
        return "member/find_id";
    }

    @PostMapping("/find_id")
    public String findId(String name, String email, Model model) {

        // 아이디 검색 서비스 호출
        Users foundUser = usersService.searchId(name, email);

        if (foundUser != null) {
            String loginId = foundUser.getLoginId();
            // 아이디가 발견된 경우
            model.addAttribute("foundId", loginId);
            model.addAttribute("isIdFound", true);
            // 아이디 발견 여부 플래그
        } else {
            model.addAttribute("isIdFound", false);
        }
        return "member/find_id";
    }

    @GetMapping("/find_pw")
    public String findPw(Model model) {
        model.addAttribute("passwordHints", PasswordHint.values());
        return "member/find_pw";
    }

    @PostMapping("/find_pw")
    public String findPw(Model model, String loginId, PasswordHint passwordHint, String passwordAnswer) {
        Users users = usersService.searchPassword(loginId, passwordHint, passwordAnswer);
        if (users != null) {
            return changePw();
        }
        model.addAttribute("errorMessage", "일치하는 정보가 없습니다.");
        return "member/find_pw";
    }

    @GetMapping("/change_pw")
    public String changePw() {
        return "member/change_pw";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);
        model.addAttribute("passwordHints", PasswordHint.values());

        if (!upperLocations.isEmpty()) {

            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocation(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UsersRequest request,
                         Model model) {
        try {
            // 전송된 데이터 로깅
            // 사용자의 Location 정보를 가져와야합니다.

            Long locationId = locationService.getLocationIdByUpperLocationAndName(request.getUpperLocation(),
                    request.getLocationName());
            request.setLocationId(locationId);

            System.out.println("Received signup request: " + request);

            return "redirect:/success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "member/signup";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "member/success";
    }

    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "member/withdrawal";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users users = usersService.findByLoginId(customUserDetails.getUsername());
        model.addAttribute("user", users);
        return "member/mypage";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // 사용자의 정보
        Users users = usersService.findUserById(getLoggedInUserId(customUserDetails));
        model.addAttribute("user", users);

        model.addAttribute("passwordHints", PasswordHint.values());

        // upperLocation의 정보
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 사용자의 현재 Location 정보 - 회원정보 수정을 누르면 원래 내 값을 가져오고 싶었으나 실패
        Location userLocation = users.getLocation();
        model.addAttribute("currentUpperLocation", userLocation.getUpperLocation()); // 사용자의 Upper Location
        model.addAttribute("locations", locationService.getLowerLocation(
                String.valueOf(userLocation.getUpperLocation()))); // 해당 Upper Location의 하위 Location 리스트

        // 해당 Upper Location의 하위 Location 리스트

        if (!upperLocations.isEmpty()) {

            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocation(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }
        return "member/edit_profile";
    }

    @PostMapping("edit_profile")
    public String editProfile(@ModelAttribute UsersRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long locationId = locationService.getLocationIdByUpperLocationAndName(request.getUpperLocation(),
                request.getLocationName());
        request.setLocationId(locationId);

        usersService.updateUser(getLoggedInUserId(customUserDetails), request);
        return "redirect:/mypage";
    }

    private Long getLoggedInUserId(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users users = usersService.findByLoginId(customUserDetails.getUsername());

        return users.getId();
    }
}