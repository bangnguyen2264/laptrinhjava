package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AuthDto implements Serializable {
    private Long id;
    private String token;
    private String refreshToken;
    private String username;
    private String role;

    public static AuthDto from(User user, String token, String refreshToken){
        return AuthDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getAuthorities().stream().findFirst().get().getAuthority())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}