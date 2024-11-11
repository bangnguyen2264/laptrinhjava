package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.form.SignInForm;
import com.project.ShopKoi.model.form.SignUpForm;
import com.project.ShopKoi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody @Valid SignInForm form){
        return ResponseEntity.ok(authService.signIn(form));
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid SignUpForm form){
        return ResponseEntity.ok(authService.signUp(form));
    }

    @GetMapping("/refresh")
    public ResponseEntity refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken){
        return ResponseEntity.ok(authService.refreshJWT(refreshToken));
    }
}
