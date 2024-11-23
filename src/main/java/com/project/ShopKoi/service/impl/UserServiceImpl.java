package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.form.UpdatePasswordForm;
import com.project.ShopKoi.repository.RoleRepository;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.UserService;
import com.project.ShopKoi.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDto getInformationUser() {
        User user = userRepository.findByEmail(UserUtils.getMe())
                .orElseThrow(() -> new IllegalArgumentException("User not sign in"));
        return UserDto.toDto(user);
    }

    @Override
    public UserDto updateInformationUser( UpdateInformationUserForm form) {
        User user = userRepository.findByEmail(UserUtils.getMe())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setFullName(form.getFullName());
        user.setDob(form.getDob());
        user.setPhone(form.getPhone());
        userRepository.save(user);
        return UserDto.toDto(user);
    }

    @Override
    public String updatePassword(UpdatePasswordForm request) {
        User user = userRepository.findByEmail(UserUtils.getMe())
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
    @Override
    public List<UserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).stream()
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUsersByRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals(role)) // Lọc theo vai trò
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return UserDto.toDto(user);
    }

}