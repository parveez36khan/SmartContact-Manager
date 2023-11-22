package com.smartcontact.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.smartcontact.entites.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	 
	 Page<Contact> findByMyuser_Id(int userId, Pageable pageable);
	 
	 long countByMyuser_Id(int userId);

}

