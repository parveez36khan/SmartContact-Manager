package com.smartcontact.config;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entites.myUser;

import org.springframework.beans.factory.annotation.Autowired;


public class UserDetailServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository userRepository ;
	 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 myUser myuser= userRepository.getUserbyUserName(username);
		 if(myuser==null) {
			 throw new UsernameNotFoundException("could not user");
		 }
		CustomUserDetails customUserDetails= new CustomUserDetails(myuser);
		return customUserDetails;
	}

}
