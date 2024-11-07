package com.example.estsoft_udon_community.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/member")
public class AdminMemberController {

    // 회원 목록
    @GetMapping("/member_list")
    public String boardList() {

        return "admin/member/member_list";
    }

    // 회원 정보 수정(등급 수정)
    @GetMapping("/member_edit")
    public String boardEdit() {

        return "admin/member/member_edit";
    }
}
