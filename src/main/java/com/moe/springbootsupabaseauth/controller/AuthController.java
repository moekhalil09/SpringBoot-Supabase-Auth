package com.moe.springbootsupabaseauth.controller;

import com.moe.springbootsupabaseauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signUp(@RequestParam String username,@RequestParam String email,@RequestParam String password) {
        userService.registerUser(username,email,password);
        return "Sign Up Successful check email for verification";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String code){
        if(userService.verifyUser(code)){
            return "Email verification successful";
        }
        return "Email verification failed";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,@RequestParam String password){
        if(userService.login(username,password)){
            return "Login successful";
        }
        return "Login failed";
    }
}
