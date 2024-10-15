package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.enums.AddressClass;
import com.project.ShopKoi.model.form.AddressItemForm;

import java.util.List;

public interface AddressItemService {

    List<AddressItemDto> findAllAddressItems();
    AddressItemDto findAddressItemById(Long id);
    AddressItemDto addAddressItem(AddressItemForm addressItemForm);
    List<AddressItemDto> addAllAddressItem(List<AddressItemForm> addressItem);
    List<AddressItemDto> findAddressItemByParentId(Long parentId);
    List<AddressItemDto> findAllAddressItemByAddressClass(AddressClass addressClass, Long parentId);
    AddressItemDto updateAddressItem(Long id,AddressItemForm addressItem);
    void deleteAddressItem(Long id);

}
