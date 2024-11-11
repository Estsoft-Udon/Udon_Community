package com.example.estsoft_udon_community.controller.admin;

import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.service.admin.AdminMemberService;
import com.example.estsoft_udon_community.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/member")
public class AdminMemberController {
    private final AdminMemberService adminMemberService;
    private final AdminService adminService;

    // 회원 목록
    @GetMapping("/member_list")
    public String boardListForAdmin(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "all") String sortOption,
                                    @RequestParam(required = false) String keyword,
                                    Model model) {
        Page<UsersResponse> allUser = adminMemberService.getFilteredUsers(page, size, sortOption, keyword)
                .map(UsersResponse::new);
        model.addAttribute("allUser", allUser);
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("keyword", keyword);

        return "admin/member/member_list";
    }
    // 회원 정보 수정(등급 수정)
    @GetMapping("/member_edit/{id}")
    public String boardEditForAdmin(Model model, @PathVariable Long id) {
        UsersResponse userById = new UsersResponse(adminMemberService.getUserById(id));
        model.addAttribute("grades", Grade.values());
        model.addAttribute("user", userById);
        return "admin/member/member_edit";
    }

    // 회원 정보 수정(등급 수정)
    @PostMapping("/member_edit/{id}")
    public String boardEditForAdmin(@PathVariable Long id, Grade grade) {
        adminService.updateUserGrade(id, grade);
        return "redirect:/admin/member/member_list";
    }
}
