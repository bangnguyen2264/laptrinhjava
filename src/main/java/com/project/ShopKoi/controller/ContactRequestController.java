package com.project.ShopKoi.controller;

import com.project.ShopKoi.service.ContactRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactRequestController {

    private final ContactRequestService contactRequestService;
}
