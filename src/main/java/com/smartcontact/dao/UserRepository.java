package com.smartcontact.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontact.entites.myUser;

public interface UserRepository extends JpaRepository<myUser, Integer> {
	
	@Query("select u from myUser u where u.email =:email")
	public myUser getUserbyUserName(@Param("email") String email);


}
