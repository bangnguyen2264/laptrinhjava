package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.entity.ContactRequest;
import com.project.ShopKoi.repository.ContactRequestRepository;
import com.project.ShopKoi.service.ContactRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ContactRequestServiceImpl implements ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;


    @Override
    public ContactRequest addContactRequest(ContactRequest contactRequest) {
        return null;
    }

    @Override
    public ContactRequest updateContactRequest(ContactRequest contactRequest) {
        return null;
    }

    @Override
    public void deleteContactRequest(Long id) {

    }

    @Override
    public ContactRequest getContactRequest(Long id) {
        return null;
    }

    @Override
    public List<ContactRequest> getAllContactRequests() {
        return List.of();
    }
}
