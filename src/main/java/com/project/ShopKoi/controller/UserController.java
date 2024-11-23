package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.form.UpdatePasswordForm;
import com.project.ShopKoi.service.AuthService;
import com.project.ShopKoi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto user = userService.getInformationUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update-info")
    public ResponseEntity<UserDto> updateUserInfo(@RequestBody @Valid UpdateInformationUserForm updateInformationUserForm) {
        UserDto updatedUser = userService.updateInformationUser(updateInformationUserForm);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateUserPassword(@RequestBody @Valid UpdatePasswordForm updatePasswordForm) {
        String responseMessage = userService.updatePassword(updatePasswordForm);
        return ResponseEntity.ok(responseMessage);
    }
}