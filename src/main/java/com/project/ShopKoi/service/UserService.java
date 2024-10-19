package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.form.UpdateInformationUserForm;
import com.project.ShopKoi.model.form.UpdatePasswordForm;

import java.security.Principal;

public interface UserService {
    UserDto getInfomationUser(Principal principal);
    UserDto updateInformationUser (Principal connectedUser, UpdateInformationUserForm form);
    String updatePassword(UpdatePasswordForm form);
}
