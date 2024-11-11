package com.project.ShopKoi.config;


import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.enums.AddressClass;
import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.repository.AddressItemRepository;
import com.project.ShopKoi.repository.RoleRepository;
import com.project.ShopKoi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class AppConfig {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressItemRepository addressItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initApp() {
        return args -> {
            if (!roleRepository.existsByName("ROLE_USER")) {
                roleRepository.save(Role.builder().id(1L).name("ROLE_USER").build());
            }
            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                roleRepository.save(Role.builder().id(2L).name("ROLE_ADMIN").build());
            }
            if (!roleRepository.existsByName("ROLE_DELIVER")) {
                roleRepository.save(Role.builder().id(3L).name("ROLE_DELIVER").build());
            }
            log.info("Roles initialized successfully");

            Role deliverRole = roleRepository.findById(3L).orElseThrow();
            Role adminRole = roleRepository.findById(2L).orElseThrow();

            if (!userRepository.existsByEmail("admin@gmail.com")) {
                userRepository.save(
                        User.builder()
                                .id(1L)
                                .fullName("admin")
                                .email("admin@gmail.com")
                                .password(passwordEncoder.encode("admin"))
                                .role(adminRole)
                                .build()
                );
                log.info("Admin initialized successfully");
            }

            if (!userRepository.existsByEmail("deliver1@gmail.com")) {
                userRepository.save(
                        User.builder()
                                .id(3L)
                                .fullName("deliver1")
                                .email("deliver1@gmail.com")
                                .password(passwordEncoder.encode("deliver"))
                                .role(deliverRole)
                                .build()
                );
                log.info("Deliver 1 initialized successfully");
            }
            if (!userRepository.existsByEmail("deliver2@gmail.com")) {
                userRepository.save(
                        User.builder()
                                .id(4L)
                                .fullName("deliver2")
                                .email("deliver2@gmail.com")
                                .password(passwordEncoder.encode("deliver"))
                                .role(deliverRole)
                                .build()
                );
                log.info("Admin 2 initialized successfully");
            }
            if(!addressItemRepository.existsByName("Việt Nam")) {
                addressItemRepository.saveAll(List.of(
                        AddressItem.builder()
                                .name("Việt Nam")
                                .addressClass(AddressClass.COUNTRY)
                                .build(),
                        AddressItem.builder()
                                .name("Nhật Bản")
                                .addressClass(AddressClass.COUNTRY)
                                .build()
                        )
                );
                log.info("Addresses initialized successfully");
            }
        };
    }
}