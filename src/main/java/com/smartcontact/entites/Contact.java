package com.smartcontact.entites;



import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="contact")
public class Contact {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int cid;

	    private String name;
	    private String email;
	    private String secondName;

	    @Column(length = 500)
	    private String descripation;
	    private String work;

	    @Size(max = 512000, message = "Image size must not exceed 500KB")
	    @Lob
	    @Column(name = "image", columnDefinition="mediumblob")
	    private byte[] image;



	    private String phone;
	    private String base64Image;
	    
	    @ManyToOne
	    private myUser myuser;
	  
	
	

	public Contact() {
		super();
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
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

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getDescripation() {
		return descripation;
	}

	public void setDescripation(String descripation) {
		this.descripation = descripation;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public myUser getMyuser() {
		return myuser;
	}

	public void setMyuser(myUser myuser) {
		this.myuser = myuser;
	}
	
	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", email=" + email + ", secondName=" + secondName
				+ ", descripation=" + descripation + ", work=" + work + ", image=" + Arrays.toString(image) + ", phone="
				+ phone + "]";
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }

	    Contact contact = (Contact) obj;
	    return this.cid == contact.getCid();
	}

	

	
	

	
}
	
	

