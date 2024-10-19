package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.form.UpdatePasswordForm;
import com.project.ShopKoi.service.AuthService;
import com.project.ShopKoi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        UserDto user = userService.getInfomationUser(principal);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update-info")
    public ResponseEntity<UserDto> updateUserInfo(Principal principal, @RequestBody UpdateInformationUserForm updateInformationUserForm) {
        UserDto updatedUser = userService.updateInformationUser(principal, updateInformationUserForm);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdatePasswordForm updatePasswordForm) {
        String responseMessage = userService.updatePassword(updatePasswordForm);
        return ResponseEntity.ok(responseMessage);
    }
}