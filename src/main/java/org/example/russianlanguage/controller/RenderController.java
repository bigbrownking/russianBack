package org.example.russianlanguage.controller;

import org.example.russianlanguage.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class RenderController {
    @GetMapping("/")
    public String home() {
        return "main";
    }
    @GetMapping("/proverbs")
    public String proverbs() {
        return "proverbs";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/reg")
    public String reg() {
        return "reg";
    }
    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }
    @GetMapping("/isLogin")
    public ResponseEntity<User> isLogin(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.OK).body((User) session.getAttribute("user"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
