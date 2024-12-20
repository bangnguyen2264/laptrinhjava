package com.project.ShopKoi.config;

import com.project.ShopKoi.security.JwtAuthenticationFilter;
import com.project.ShopKoi.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Các endpoint công khai
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        // Chỉ có Admin có thể truy cập đến các chức năng của Address Items
                        .requestMatchers(HttpMethod.GET, "/api/v1/address-items/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/address-items/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/address-items/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/address-items/**").hasAnyAuthority("ROLE_ADMIN")
                        // Cấu hình cho API đơn hàng
                        .requestMatchers(HttpMethod.POST, "/api/v1/order").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/order/me/").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/order", "/api/v1/order/number/**").hasAnyAuthority("ROLE_ADMIN") // Admin có thể xem tất cả đơn hàng
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/order/change-status/**").hasAnyAuthority("ROLE_ADMIN","ROLE_DELIVER") // Admin có thể thay đổi trạng thái đơn hàng
                        .requestMatchers("/api/v1/order/deliver/**").hasAnyAuthority("ROLE_DELIVER", "ROLE_ADMIN") // Nhân viên giao hàng và Admin có thể cập nhật trạng thái giao hàng
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/order/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // Admin có thể xóa đơn hàng
                        // Cấu hình quyền cho người dùng
                        .requestMatchers("/api/v1/user/**").authenticated()
                        // Cấu hình quyền cho Admin
                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_ADMIN") // Chỉ Admin có quyền truy cập các API admin
                        //Cấu hình quyền quản lý yêu cầu liên hệ
                        .requestMatchers(HttpMethod.POST, "/api/v1/contact").permitAll() // Cho phép mọi người tạo yêu cầu liên hệ
                        .requestMatchers(HttpMethod.GET, "/api/v1/contact/**").hasAnyAuthority("ROLE_ADMIN") // Chỉ Admin có thể xem các yêu cầu liên hệ
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/contact/**").hasAnyAuthority("ROLE_ADMIN") // Chỉ Admin có thể xóa các yêu cầu liên hệ
                        // Các yêu cầu còn lại yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration cfg = new CorsConfiguration();
                            cfg.setAllowedOrigins(Collections.singletonList("*"));
                            cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                            cfg.setAllowedHeaders(List.of("*"));
                            return cfg;
                        })
                )
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private String[] getPublicEndpoints() {
        return new String[]{
                "/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
