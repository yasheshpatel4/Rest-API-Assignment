package com.example.REST_API.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/login")
    public ResponseEntity<String> login(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "dummy")
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Cookie set! You are now authenticated.");
    }

//    @GetMapping("/test")
//    public String test() {
//        return "Access Granted: You have the dummy cookie!";
//    }
}
