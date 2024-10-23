package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.enums.AddressClass;
import com.project.ShopKoi.model.form.AddressItemForm;

import java.awt.print.Pageable;
import java.util.List;

public interface AddressItemService {

    List<AddressItemDto> findAllAddressItems(int page, int size);
    AddressItemDto findAddressItemById(Long id);
    AddressItemDto addAddressItem(AddressItemForm addressItemForm);
    String addAllAddressItem(List<AddressItemForm> addressItem);
    List<AddressItemDto> findAddressItemByParentId(Long parentId);
    List<AddressItemDto> findAllAddressItemByAddressClass(AddressClass addressClass, Long parentId);
    AddressItemDto updateAddressItem(Long id,AddressItemForm addressItem);
    void deleteAddressItem(Long id);

}
