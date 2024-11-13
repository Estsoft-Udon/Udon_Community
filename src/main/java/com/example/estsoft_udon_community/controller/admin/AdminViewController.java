package com.example.estsoft_udon_community.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminViewController {

    @GetMapping("/admin")
    public String adminMain() {
        return "admin/admin_index";
    }
}
