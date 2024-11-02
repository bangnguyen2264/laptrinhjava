package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.entity.ContactRequest;
import com.project.ShopKoi.model.form.ContactRequestForm;
import com.project.ShopKoi.service.ContactRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    @GetMapping
    public ResponseEntity getAllContactRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contactRequestService.getAllContactRequests(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity getContactRequestById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactRequestService.getContactRequest(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteContactRequestById(@PathVariable("id") Long id) {
        contactRequestService.deleteContactRequest(id);
        return ResponseEntity.ok("Deleted contact request with id " + id);
    }
    @PostMapping
    public ResponseEntity createContactRequest(@RequestBody @Valid ContactRequestForm contactRequest) {
        return ResponseEntity.ok(contactRequestService.addContactRequest(contactRequest));
    }
}
