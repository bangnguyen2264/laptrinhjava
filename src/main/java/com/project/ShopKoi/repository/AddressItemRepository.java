package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.entity.AddressItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressItemRepository extends JpaRepository<AddressItem, Long> {
    List<AddressItem> findByParentId(Long parentId);
    boolean existsByParentId(Long parentId);
}
