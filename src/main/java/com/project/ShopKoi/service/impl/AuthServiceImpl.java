package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.InvalidRefreshTokenException;
import com.project.ShopKoi.model.dto.AuthDto;
import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.form.SignInForm;
import com.project.ShopKoi.model.form.SignUpForm;
import com.project.ShopKoi.repository.RoleRepository;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.security.JwtService;
import com.project.ShopKoi.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthDto signIn(SignInForm signInForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword())
        );

        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);

        User user = userRepository.findByEmail(signInForm.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + signInForm.getEmail()));

        log.info("User {} logged in", user.getUsername());
        return AuthDto.from(user, accessToken, refreshToken);
    }

    @Override
    public String signUp(SignUpForm signUpForm) {
        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Not found role ROLE_USER"));

        User user = User.builder()
                .fullName(signUpForm.getFullName())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        log.info("User {} registered", user.getUsername());
        return "Success register new user";
    }

    @Override
    public AuthDto refreshJWT(String refreshToken) {
        if (refreshToken != null) {
            refreshToken = refreshToken.replaceFirst("Bearer ", "");
            if (jwtService.validateRefreshToken(refreshToken)) {
                Authentication auth = jwtService.createAuthentication(refreshToken);

                User user = userRepository.findByEmail(auth.getName())
                        .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + auth.getName()));

                log.info("User {} refreshed token", user.getUsername());
                return AuthDto.from(user, jwtService.generateAccessToken(auth), refreshToken);
            }
        }
        throw new InvalidRefreshTokenException(refreshToken);
    }
}
