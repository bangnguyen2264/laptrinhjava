package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.form.AddressItemForm;

import java.util.List;

public interface AddressItemService {

    List<AddressItemDto> findAllAddressItems();
    AddressItemDto findAddressItemById(Long id);
    List<AddressItemDto> addAllAddressItem(List<AddressItemForm> addressItem);
    List<AddressItemDto> findAddressItemByParentId(Long parentId);
    AddressItemDto firstAddressItemByChildrenId(Long childrenId);
    AddressItemDto updateAddressItem(AddressItemForm addressItem);
    void deleteAddressItem(Long id);

}
