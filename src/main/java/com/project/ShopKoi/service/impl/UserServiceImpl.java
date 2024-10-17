package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.form.UpdatePasswordForm;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
    public String updatePassword(UpdatePasswordForm form, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!form.getOldPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        user.setPassword(form.getNewPassword());
        userRepository.save(user);
        return "Mật khẩu đã được thay đổi thành công";
    }
}
