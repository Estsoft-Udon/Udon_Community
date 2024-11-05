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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UsersService usersService;
    private final LocationService locationService;
    private final UsersDetailService usersDetailService;
    private final BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login() {
        return "member/login";
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

            //
            Long locationId = locationService.getLocationIdByUpperLocationAndName(request.getUpperLocation(),
                    request.getLocationName());
            request.setLocationId(locationId);

            usersService.registerUser(request);
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
    public String mypage(@AuthenticationPrincipal CustomUserDetails userDetail, Model model) {
        getCurrentUser();
        Users users = usersService.findByLoginId(userDetail.getUsername());
        model.addAttribute("user", users);
        return "member/mypage";
    }

    @GetMapping("/edit_profile")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails userDetail, Model model) {
        Users users = usersService.findByLoginId(userDetail.getUsername());
        model.addAttribute("user", users);
        return "member/edit_profile";
    }

    public void getCurrentUser() {
        System.out.println(SecurityContextHolder.getContext());
    }
}