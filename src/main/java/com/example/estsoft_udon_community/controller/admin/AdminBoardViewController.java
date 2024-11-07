package com.example.estsoft_udon_community.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/board")
public class AdminBoardViewController {

    // 게시글 목록
    @GetMapping("/board_list")
    public String boardList() {

        return "admin/board/board_list";
    }

    // 게시글 수정(공개/비공개)
    @GetMapping("/board_edit")
    public String boardEdit() {

        return "admin/board/board_edit";
    }
}
