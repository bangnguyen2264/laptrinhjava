package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = ?1")
    long countByRole(Role role);
}
