package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.form.UpdatePasswordForm;

import java.util.List;

public interface UserService {
    UserDto getInformationUser();
    UserDto updateInformationUser ( UpdateInformationUserForm form);
    String updatePassword(UpdatePasswordForm form);
    List<UserDto> getAllUsers();
    List<UserDto> getAllUsersByRole(String roleName);
    UserDto getUserById(Long id);
}
