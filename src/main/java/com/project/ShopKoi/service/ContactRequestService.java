package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.ContactRequestDto;
import com.project.ShopKoi.model.entity.ContactRequest;
import com.project.ShopKoi.model.form.ContactRequestForm;

import java.util.List;

public interface ContactRequestService {

    String addContactRequest(ContactRequestForm contactRequest);
    void deleteContactRequest(Long id);
    ContactRequestDto getContactRequest(Long id);
    List<ContactRequestDto> getAllContactRequests(int page, int size);

}
