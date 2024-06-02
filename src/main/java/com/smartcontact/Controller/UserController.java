package com.smartcontact.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;	
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartcontact.dao.ContactRepository;
import com.smartcontact.dao.UserRepository;
import com.smartcontact.entites.Contact;
import com.smartcontact.entites.myUser;
import com.smartcontact.helper.Message;
import com.smartcontact.service.ContactService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/myuser")
public class UserController {
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  ContactRepository contactRepository;
	
	 @Autowired
	 private ContactService contactService;
	
	@ModelAttribute
	public void addCommonData(Model model ,Principal principal) {
		 String username= principal.getName();
		 System.out.println(username);
		myUser myuser= this.userRepository.getUserbyUserName(username);
		model.addAttribute("myuser", myuser);
		System.out.println(myuser);

		
	}
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
	    model.addAttribute("title", "Dashboard");

	    String username = principal.getName();
	    myUser user = this.userRepository.getUserbyUserName(username);
	    model.addAttribute("user", user);

	    // Retrieve the user's profile image
	    String base64Image = user.getBase64Image();
	    if (base64Image == null || base64Image.isEmpty()) {
	        // If the user's profile image is not available, use a default image
	        base64Image = getDefaultImageBase64(); // Implement this method to get the default image
	    }
	    model.addAttribute("profileImage", base64Image);
	    if (user.getImageUrl() != null) {
            String base64Imagee = Base64.getEncoder().encodeToString(user.getImageUrl());
            user.setBase64Image(base64Imagee);
        } else {
            System.out.println("Using default image for contact with null image.");
            // Handle the case when the image is null by setting a default image.
            String defaultImageBase64 = getDefaultImageBase64();
            user.setBase64Image(defaultImageBase64);
        }
	    // Add any other necessary data to the model

	    return "normal/user_dashboard";
	}

	@GetMapping("/addcontact")
	public String add_Contact(Model model) {
		model.addAttribute("title","contact");
		model.addAttribute("contact", new Contact());
		return "normal/addcontact";
	}

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact,
	                             @RequestParam("fileimage") MultipartFile file,
	                             Principal principal,HttpSession session,BindingResult result3,Model model) {
	    try {
	        String name = principal.getName();
	        myUser myuser = this.userRepository.getUserbyUserName(name);
	        if((file.isEmpty())) {
	        	System.out.println("file is empty");
	        	 contact.setImage(getDefaultImageBytes("static/image/contact1.jpg"));
	        }
	        
	        else {
	            // Set the content of the file as the image in your Contact entity
	            contact.setImage(file.getBytes());

	            // Save the file to the file system if needed (you may want to check if the directory exists)
	            File saveFile = new ClassPathResource("static/image").getFile();
	            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	            System.out.println("Image uploaded successfully");
	        }
	        
	        if (result3.hasErrors()) {
	        	System.out.println("error:" + result3.toString() );
	        	model.addAttribute("contact", contact);
	            return "normal/addcontact"; // Return the same form template with error messages
	        }

	        contact.setMyuser(myuser);
	        myuser.getContacts().add(contact);
	        this.userRepository.save(myuser);

	        System.out.println(contact);
	        session.setAttribute("message", new Message("you added successfully:!!add more","success"));

	    } catch (Exception e) {
	        System.out.println("Error: " + e.getMessage());
	        e.printStackTrace();
	        session.setAttribute("message", new Message("something went wrong!!:try again","error"));
	    }
	    

	    return "normal/addcontact";
	}
	private String getDefaultImageBase64() {
	    try {
	        // Load the default image as a resource
	        ClassPathResource resource = new ClassPathResource("static/image/contact1.jpg");
	        
	        if (resource.exists()) {
	            byte[] defaultImageBytes = Files.readAllBytes(resource.getFile().toPath());
	            return Base64.getEncoder().encodeToString(defaultImageBytes);
	        } else {
	            System.out.println("Default image does not exist at: " + resource.getURL());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return ""; // Return an empty string if there's an issue with the default image.
	}

	private byte[] getDefaultImageBytes(String imagePath) throws IOException {
	    // The imagePath should be relative to the classpath, which is "src/main/resources" in your project.
	    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath);

	    if (inputStream != null) {
	        try {
	            // Read the default image file from the classpath and convert it to a byte array
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	            return outputStream.toByteArray();
	        } catch (IOException e) {
	            // Log the exception for further investigation
	            e.printStackTrace();
	        } finally {
	            // Close the input stream when done
	            if (inputStream != null) {
	                inputStream.close();
	            }
	        }
	    } else {
	        // Handle the case when the default image doesn't exist
	        // Log an informative message here
	        System.out.println("Default image does not exist at: " + imagePath);
	    }

	    // If there's an issue, return an empty byte array
	    return new byte[0];
	}

	@GetMapping("/Show-Contact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model m, Principal principal) {
	    m.addAttribute("title", "This is the show contact page");

	    String name = principal.getName();
	    myUser myuser = this.userRepository.getUserbyUserName(name);

	    System.out.println("User: " + myuser);

	    Pageable pageable = PageRequest.of(page, 5); // Example: 5 contacts per page

	    Page<Contact> contacts = this.contactRepository.findByMyuser_Id(myuser.getId(), pageable);
	    System.out.println("Total Pages: " + contacts.getTotalPages());
	
	    for (Contact contact : contacts) {
	        if (contact.getImage() != null) {
	            String base64Image = Base64.getEncoder().encodeToString(contact.getImage());
	            contact.setBase64Image(base64Image);
	        } else {
	            System.out.println("Using default image for contact with null image.");
	            // Handle the case when the image is null by setting a default image.
	            String defaultImageBase64 = getDefaultImageBase64();
	            contact.setBase64Image(defaultImageBase64);
	        }
	    }
	   
	    m.addAttribute("contacts", contacts);
	    m.addAttribute("currentPage",page);
	    m.addAttribute("totalPages", contacts.getTotalPages());
	    System.out.println("Total Contacts: " + this.contactRepository.countByMyuser_Id(myuser.getId()));

	    return "normal/Show-Contact";
	}
    

    @RequestMapping("/{cid}/contact")
    public String showContactDetails(@PathVariable("cid") int cid,Model m,Principal principal) {
    	 Optional<Contact> contactOptional = this.contactRepository.findById(cid);
         Contact contact = contactOptional.get();

         if (contact.getImage() != null) {
             String base64Image = Base64.getEncoder().encodeToString(contact.getImage());
             contact.setBase64Image(base64Image);
         } else {
             System.out.println("Using default image for contact with null image.");
             // Handle the case when the image is null by setting a default image.
             String defaultImageBase64 = getDefaultImageBase64();
             contact.setBase64Image(defaultImageBase64);
         }
         String username= principal.getName();
         myUser user= this.userRepository.getUserbyUserName(username);
         if(user.getId()==contact.getMyuser().getId()) {
         	m.addAttribute("contact", contact);
         	m.addAttribute("title", contact.getName());
         }
        return "normal/contact_details";
    }
    @GetMapping("/delete/{cid}")
    @Transactional
    public String deleteContact(@PathVariable("cid") int  cid,Model m,HttpSession session ,Principal principal) {
    	try {
    	    // Your existing code here
    		System.out.println("CID :"+ cid);
      	  Contact contact  = this.contactRepository.findById(cid).get();
      	  String username= principal.getName();
            myUser user= this.userRepository.getUserbyUserName(username);
            user.getContacts().remove(contact);
            this.userRepository.save(user);
            session.setAttribute("message",  new Message("contact deleted","success"));
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    // Log the exception or add additional error handling
    	}
    	
         // Redirect to the contact list page
        return"redirect:/myuser/Show-Contact/0";
    } 


    @PostMapping("/update/{cid}")
    public String showUpdateContactForm(@PathVariable int cid, Model model) {
        Contact contact =this.contactRepository.findById(cid).get();
        model.addAttribute("title","updateForm");
        model.addAttribute("contact", contact);
        return "normal/updateForm"; // You need to create an HTML template for updating a contact
    }
    @PostMapping("/update-contact")
    public String updateContact(@ModelAttribute("contact") Contact updatedContact,
                                @RequestParam("fileimage") MultipartFile fileImage,
                                RedirectAttributes redirectAttributes,
                                Model model, HttpSession session, Principal principal) {

        try {
            // Retrieve the existing contact from the database
            Contact existingContact = this.contactRepository.findById(updatedContact.getCid()).get();

            if (existingContact != null) {
                // Update the fields with the new values
                existingContact.setName(updatedContact.getName());
                existingContact.setSecondName(updatedContact.getSecondName());
                existingContact.setEmail(updatedContact.getEmail());
                existingContact.setWork(updatedContact.getWork());
                existingContact.setPhone(updatedContact.getPhone());

                // Handle image update (if a new image is provided)
                if (!fileImage.isEmpty()) {
                    // Update the image logic here
                    // For example, save the new image to the server and update the contact's image URL
                    // You may use a service or utility class to handle image-related operations
                }

                // Save the updated contact
                this.contactRepository.save(existingContact);

                // Set a success message
                redirectAttributes.addFlashAttribute("message", new Message("Contact updated successfully", "success"));
            } else {
                // Set an error message if the contact is not found
                redirectAttributes.addFlashAttribute("message", new Message("Contact not found", "danger"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception or add additional error handling
        }

        // Redirect to the contact details page or a suitable page
        return "redirect:/myuser/Show-Contact/0" ;
    }
    @RequestMapping("/profile")
    public String profile(Model m, Principal principal) {
    	m.addAttribute("title", "this is profile page");
    	String name=principal.getName();
    	myUser user=this.userRepository.getUserbyUserName(name);
    	m.addAttribute("user", user);
    	if (user.getImageUrl() != null) {
            String base64Imagee = Base64.getEncoder().encodeToString(user.getImageUrl());
            user.setBase64Image(base64Imagee);
        } else {
            System.out.println("Using default image for contact with null image.");
            // Handle the case when the image is null by setting a default image.
            String defaultImageBase64 = getDefaultImageBase64();
            user.setBase64Image(defaultImageBase64);
        }
    	return "normal/profileForm";
    }

    
    
    
    
    
}
