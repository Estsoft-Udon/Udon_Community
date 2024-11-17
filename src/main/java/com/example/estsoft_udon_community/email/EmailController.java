package com.example.estsoft_udon_community.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final AuthCodeService authCodeService;

    @ResponseBody
    @PostMapping("/send-email")
    public String sendEmail(@RequestParam(required = false) String email) {
        String authCode = AuthCodeGenerator.generateCode(6); // 6자리 랜덤 숫자 생성
        authCodeService.saveAuthCode(email, authCode);
        emailService.sendEmail(email, authCode);
        return "이메일 전송 완료!";
    }
}
