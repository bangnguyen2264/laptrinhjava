package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.form.AddressItemForm;
import com.project.ShopKoi.service.AddressItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AddressController {

    private final AddressItemService addressItemService;

    // 1. Lấy tất cả AddressItem
    @GetMapping
    public ResponseEntity<List<AddressItemDto>> getAllAddressItems() {
        List<AddressItemDto> addressItems = addressItemService.findAllAddressItems();
        return ResponseEntity.ok(addressItems);
    }

    // 2. Lấy AddressItem theo id
    @GetMapping("/{id}")
    public ResponseEntity<AddressItemDto> getAddressItemById(@PathVariable Long id) {
        AddressItemDto addressItem = addressItemService.findAddressItemById(id);
        return ResponseEntity.ok(addressItem);
    }

    // 3. Thêm một hoặc nhiều AddressItem
    @PostMapping
    public ResponseEntity<List<AddressItemDto>> addAddressItems(@RequestBody List<AddressItemForm> addressItemForms) {
        List<AddressItemDto> createdItems = addressItemService.addAllAddressItem(addressItemForms);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
    }

    // 4. Cập nhật AddressItem
    @PutMapping("/{id}")
    public ResponseEntity<AddressItemDto> updateAddressItem(@PathVariable Long id, @RequestBody AddressItemForm addressItemForm) {
        AddressItemDto updatedAddressItem = addressItemService.updateAddressItem(addressItemForm);
        return ResponseEntity.ok(updatedAddressItem);
    }

    // 5. Xóa AddressItem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddressItem(@PathVariable Long id) {
        addressItemService.deleteAddressItem(id);
        return ResponseEntity.noContent().build();
    }
}
