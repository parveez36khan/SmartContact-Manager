package com.smartcontact.entites;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="myUser")
public class myUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

	private String about ;
	private String imageUrl;
	private String role ;
	private boolean enable;
	
	
	public myUser() {
		super();
	}
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "myuser")
	 private List<Contact>  contacts=new ArrayList<>();

	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getImageurl() {
		return imageUrl;
	}
	public void setImageurl(String imageurl) {
		this.imageUrl = imageurl;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	@Override
	public String toString() {
		return "myUser [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", about="
				+ about + ", imageUrl=" + imageUrl + ", role=" + role + ", enable=" + enable + ", contacts=" + contacts
				+ "]";
	}
	
}
