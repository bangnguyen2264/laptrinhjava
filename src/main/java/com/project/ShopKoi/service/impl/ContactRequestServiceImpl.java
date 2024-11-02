package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.NotFoundException;
import com.project.ShopKoi.model.dto.ContactRequestDto;
import com.project.ShopKoi.model.entity.ContactRequest;
import com.project.ShopKoi.model.form.ContactRequestForm;
import com.project.ShopKoi.repository.ContactRequestRepository;
import com.project.ShopKoi.service.ContactRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ContactRequestServiceImpl implements ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;


    @Override
    public String addContactRequest(ContactRequestForm contactRequest) {
        new ContactRequestForm();
        ContactRequest newContactRequest = ContactRequestForm.toEntity(contactRequest);
        contactRequestRepository.save(newContactRequest);
        return "Add Contact Request Successfully";
    }

    @Override
    public void deleteContactRequest(Long id) {
        ContactRequest contactRequest = contactRequestRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Contact Request Not Found")
        );
        contactRequestRepository.delete(contactRequest);
    }

    @Override
    public ContactRequestDto getContactRequest(Long id) {
        ContactRequest contactRequest = contactRequestRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Contact Request Not Found")
        );
        return ContactRequestDto.toDto(contactRequest);
    }

    @Override
    public List<ContactRequestDto> getAllContactRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contactRequestRepository
                .findAll(pageable)
                .stream().map(ContactRequestDto::toDto).toList();
    }
}
