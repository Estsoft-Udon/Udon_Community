package com.example.estsoft_udon_community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    @GetMapping("/board_list")
    public String boardList() {
        return "board/board_list";
    }
    @GetMapping("/board_detail")
    public String boardDetail() {
        return "board/board_detail";
    }
    @GetMapping("/board_edit")
    public String boardEdit() {
        return "board/board_edit";
    }
}
