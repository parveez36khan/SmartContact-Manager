package com.smartcontact.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    @Bean
	public UserDetailsService getUserDetailsService(){
		return new UserDetailServiceImpl();
	} 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider AuthenticationProvider() {
    	DaoAuthenticationProvider daoAuthenticationProvider =new DaoAuthenticationProvider();
    	daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    	
    	return daoAuthenticationProvider; 
    }

    @SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
    	http.authorizeRequests()
        .requestMatchers("/admin/**").hasRole("Admin")
        .requestMatchers("/myuser/**").hasRole("USER")
        .requestMatchers("/**").permitAll()
        .and()
        .formLogin().loginPage("/signin")
        .and()
        .csrf().disable();

		http.formLogin().defaultSuccessUrl("/myuser/index", true);
		http.authenticationProvider(AuthenticationProvider());
		
		return http.build();
	}
}
