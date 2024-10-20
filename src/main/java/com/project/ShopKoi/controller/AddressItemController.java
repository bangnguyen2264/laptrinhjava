package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.AddressItemDto;
import com.project.ShopKoi.model.enums.AddressClass;
import com.project.ShopKoi.model.form.AddressItemForm;
import com.project.ShopKoi.service.AddressItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address-items")
@RequiredArgsConstructor
public class AddressItemController {
    private final AddressItemService addressItemService;

    @GetMapping
    public ResponseEntity<List<AddressItemDto>> getAllAddressItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<AddressItemDto> addressItems = addressItemService.findAllAddressItems(page, size);
        return ResponseEntity.ok(addressItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressItemDto> getAddressItemById(@PathVariable Long id) {
        AddressItemDto addressItem = addressItemService.findAddressItemById(id);
        return ResponseEntity.ok(addressItem);
    }

    @PostMapping
    public ResponseEntity<AddressItemDto> addAddressItem(@RequestBody @Valid AddressItemForm addressItemForm) {
        AddressItemDto newAddressItem = addressItemService.addAddressItem(addressItemForm);
        return ResponseEntity.ok(newAddressItem);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<AddressItemDto>> addAllAddressItems(@RequestBody List<AddressItemForm> addressItemForms) {
        List<AddressItemDto> newAddressItems = addressItemService.addAllAddressItem(addressItemForms);
        return ResponseEntity.ok(newAddressItems);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<AddressItemDto>> getAddressItemsByParentId(@PathVariable Long parentId) {
        List<AddressItemDto> addressItems = addressItemService.findAddressItemByParentId(parentId);
        return ResponseEntity.ok(addressItems);
    }
    @GetMapping("/class")
    public ResponseEntity<List<AddressItemDto>> getAddressItemByAddressClass(@RequestParam AddressClass addressClass,
                                                                             @RequestParam(required = false) Long parentId) {
        List<AddressItemDto> addressItems = addressItemService.findAllAddressItemByAddressClass(addressClass, parentId);
        return ResponseEntity.ok(addressItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressItemDto> updateAddressItem(@PathVariable Long id, @RequestBody @Valid AddressItemForm addressItemForm) {
        AddressItemDto updatedAddressItem = addressItemService.updateAddressItem(id,addressItemForm);
        return ResponseEntity.ok(updatedAddressItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddressItem(@PathVariable Long id) {
        addressItemService.deleteAddressItem(id);
        return ResponseEntity.ok("Deleted AddressItem successfully.") ;
    }
}
