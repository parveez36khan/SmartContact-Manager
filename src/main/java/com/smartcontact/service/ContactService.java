package com.smartcontact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcontact.dao.ContactRepository;
import com.smartcontact.entites.Contact;

import jakarta.transaction.Transactional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    public void deleteContactById(int contactId) {
        contactRepository.deleteById(contactId);
    }
    
    public Contact getContactById(int contactId) {
        return contactRepository.findById(contactId).orElse(null);
    }
}
