package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.AuthDto;
import com.project.ShopKoi.model.form.SignInForm;
import com.project.ShopKoi.model.form.SignUpForm;

public interface AuthService {

    AuthDto signIn(SignInForm signInForm);
    String signUp(SignUpForm signUpForm);
    AuthDto refreshJWT(String refreshToken);
}
