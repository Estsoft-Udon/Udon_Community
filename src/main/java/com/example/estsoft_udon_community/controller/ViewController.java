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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UsersService usersService;
    private final LocationService locationService;
    Long userId;


    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    // 로그인
    @PostMapping("/login")
    public String loginPost(@RequestParam String loginId,
                            @RequestParam String password,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            Users users = usersService.loginUser(loginId, password);
            userId = users.getId();
            redirectAttributes.addFlashAttribute("successMessage", "로그인 성공!");
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
            // 사용자의 Location 정보를 가져와야합니다.
            //
            Long locationId = request.getLocationId();
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
        Users users = usersService.findUserById(getLoggedInUserId());
        model.addAttribute("user", users);
        return "member/mypage";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model) {
        Users users = usersService.findUserById(getLoggedInUserId());
        model.addAttribute("user", users);
        return "member/edit_profile";
    }

    private Long getLoggedInUserId() {
        // SecurityContextHolder 또는 세션으로 가져와야 한다.
        return userId;
    }
}