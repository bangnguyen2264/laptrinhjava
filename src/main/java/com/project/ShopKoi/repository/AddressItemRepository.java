package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.enums.AddressClass;
import com.project.ShopKoi.model.entity.AddressItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressItemRepository extends JpaRepository<AddressItem, Long> {
    List<AddressItem> findAddressItemByParentId(Long parentId);
    List<AddressItem> findAddressItemByAddressClass(AddressClass addressClass);
    boolean existsByName(String name);

    List<AddressItem> findAddressItemByAddressClassAndParentId(AddressClass addressClass, Long parentId);

    Optional<AddressItem> findByName(String name);
}
