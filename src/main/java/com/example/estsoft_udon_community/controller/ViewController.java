package com.example.estsoft_udon_community.controller;

import static com.example.estsoft_udon_community.util.SecurityUtil.*;

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

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UsersService usersService;
    private final LocationService locationService;

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
        // 비밀번호 찾기 성공
        if (users != null) {
            return changePw();
        }
        // 비밀번호 찾기 실패
        model.addAttribute("errorMessage", "일치하는 정보가 없습니다.");
        return "member/find_pw";
    }

    @GetMapping("/change_pw")
    public String changePw() {
        return "member/change_pw";
    }

    // 회원가입 
    @GetMapping("/signup")
    public String signup(Model model) {
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);
        model.addAttribute("passwordHints", PasswordHint.values());

        if (!upperLocations.isEmpty()) {
            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocations(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }

        return "member/signup";
    }
    
    @PostMapping("/signup")
    public String signup(@ModelAttribute UsersRequest request,
                         Model model, Long locationId) {
        try {
            request.setLocationId(locationId);
            usersService.registerUser(request);
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
        Users userById = usersService.findUserById(getLoggedInUser().getId());
        model.addAttribute("user", userById);
        return "member/mypage";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model) {
        // 로그인 사용자의 정보
        Users users = usersService.findUserById(getLoggedInUser().getId());
        model.addAttribute("user", users);
        model.addAttribute("passwordHints", PasswordHint.values());

        // upperLocation의 정보
        List<String> upperLocations = locationService.getDistinctUpperLocations();
        model.addAttribute("upperLocations", upperLocations);

        // 사용자의 마이페이지 Location 정보
        Location userLocation = users.getLocation();
        model.addAttribute("userLocation", userLocation);

        // 해당 Upper Location의 하위 Location 리스트
        if (!upperLocations.isEmpty()) {

            String firstUpperLocation = upperLocations.get(0);
            List<Location> lowerLocations = locationService.getLowerLocations(firstUpperLocation);
            model.addAttribute("locations", lowerLocations);
        }
        return "member/edit_profile";
    }

    @PostMapping("/edit_profile")
    public String editProfile(@ModelAttribute UsersRequest request,Long locationId) {
        request.setLocationId(locationId);

        usersService.updateUser(getLoggedInUser().getId(), request);
        return "redirect:/mypage";
    }
}