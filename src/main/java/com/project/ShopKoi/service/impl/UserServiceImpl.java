package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.form.UpdatePasswordForm;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.UserService;
import com.project.ShopKoi.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getInfomationUser() {
        return userRepository.findByEmail(UserUtils.getMe())
                .map(UserDto::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not signed in"));
    }

    @Override
    public UserDto updateInformationUser(UpdateInformationUserForm form) {
        User user = findUserByEmail();

        updateUserFields(user, form);

        userRepository.save(user);
        return UserDto.toDto(user);
    }

    @Override
    public String updatePassword(UpdatePasswordForm form) {
        User user = findUserByEmail();

        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            return "Current password is incorrect";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            return "New password and confirm password do not match";
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

    // Tách logic tìm kiếm user thành một phương thức riêng
    private User findUserByEmail() {
        return userRepository.findByEmail(UserUtils.getMe())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Phương thức tiện ích để cập nhật thông tin user
    private void updateUserFields(User user, UpdateInformationUserForm form) {
        Optional.ofNullable(form.getFullName()).ifPresent(user::setFullName);
        Optional.ofNullable(form.getDob()).ifPresent(user::setDob);
        Optional.ofNullable(form.getPhone()).ifPresent(user::setPhone);
    }
}
