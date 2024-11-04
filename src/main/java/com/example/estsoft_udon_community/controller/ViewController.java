package com.example.estsoft_udon_community.controller;

import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.PasswordHint;
import com.example.estsoft_udon_community.service.LocationService;
import com.example.estsoft_udon_community.service.UsersService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UsersService usersService;
    private final LocationService locationService;

    private Long userId;


    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    // 로그인은 수정해서
    @PostMapping("/login")
    public String loginPost(@RequestParam String loginId,
                            @RequestParam String password,
                            Model model) {
        try {
            Users users = usersService.loginUser(loginId, password);
            userId = users.getId();

            return "redirect:/mypage";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "member/login"; // 로그인 실패 시 로그인 페이지로 다시 이동
        }
    }

    @GetMapping("/find_id")
    public String findId() {
        return "member/find_id";
    }

    @GetMapping("/find_pw")
    public String findPw() {
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
    public String mypage(Model model) {
        Users users = usersService.findUserById(getLoggedInUserId());
        model.addAttribute("user", users);
        return "member/mypage";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model) {
        // 사용자의 정보
        Users users = usersService.findUserById(getLoggedInUserId());
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
    public String editProfile(@ModelAttribute UsersRequest request) {
        Long locationId = locationService.getLocationIdByUpperLocationAndName(request.getUpperLocation(),
                request.getLocationName());
        request.setLocationId(locationId);

        usersService.updateUser(getLoggedInUserId(), request);
        return "redirect:/mypage";
    }

    private Long getLoggedInUserId() {
        // SecurityContextHolder 또는 세션으로 가져와야 한다.
        return userId;
    }
}