package com.project.ShopKoi.service;

import com.project.ShopKoi.model.entity.ContactRequest;

import java.util.List;

public interface ContactRequestService {

    ContactRequest addContactRequest(ContactRequest contactRequest);
    ContactRequest updateContactRequest(ContactRequest contactRequest);
    void deleteContactRequest(Long id);
    ContactRequest getContactRequest(Long id);
    List<ContactRequest> getAllContactRequests();

}
