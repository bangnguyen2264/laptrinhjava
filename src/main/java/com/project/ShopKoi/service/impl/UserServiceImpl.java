package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.form.UpdatePasswordForm;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Override
    public UserDto getInfomationUser(Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDto.toDto(user);
    }

    @Override
    public UserDto updateInformationUser(Principal connectedUser, UpdateInformationUserForm form) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (form.getFullName() != null) {
            user.setFullName(form.getFullName());
        }
        if (form.getPhone() != null) {
            user.setPhone(form.getPhone());
        }
        if (form.getAddress() != null) {
            user.setAddress(form.getAddress());
        }
        userRepository.save(user);
        return UserDto.toDto(user);
    }

    @Override
    public String updatePassword(UpdatePasswordForm request, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return "Current password is incorrect";
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "New password and confirm password do not match";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

}
