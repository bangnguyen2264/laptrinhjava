package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a JOIN a.addressItems ai WHERE a.name = :name AND ai IN :addressItems")
    Optional<Address> findByNameAndAddressItems(@Param("name") String name, @Param("addressItems") List<AddressItem> addressItems);
}
