package com.smartcontact.Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entites.myUser;
import com.smartcontact.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
    private UserRepository userRepository;
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "this is homepage");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "this is aboutpage");
		return "about";
	}
	@RequestMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("title", "this is signUp page");
		model.addAttribute("myuser", new myUser());
		return "signup";
	}
	@RequestMapping("/signin")
	public String Customlogin(Model model) {
		model.addAttribute("title", "this is login page");
		model.addAttribute("myuser", new myUser());
		return "login";
	}
	
	
	  @GetMapping("/register")
	  public String showRegistrationForm(Model model) {
	     model.addAttribute("myuser", new myUser());
	     return "signup";
	      
	  }
	  
	 @PostMapping("/register")
	 public String registerUser(@Valid @ModelAttribute("myuser") myUser myuser,@RequestParam("fileimage") MultipartFile file,BindingResult result2,@RequestParam(value="agreement",defaultValue ="false") boolean agreement ,Model model,HttpSession session ) { // Perform user
		try {
			if(!agreement) {
				System.out.println("you have not agreed terms and conditions");
				throw new Exception("you have not agreed terms and conditions");
			}
			
			if(result2.hasErrors()) {
				System.out.println("error:" + result2.toString() );
				model.addAttribute("myuser", myuser);
				return "signup";
			}
			if((file.isEmpty())) {
	        	System.out.println("file is empty");
	        	 myuser.setImageUrl(getDefaultImageBytes("static/image/contact1.jpg"));
	        }
	        
	        else {
	            // Set the content of the file as the image in your Contact entity
	            myuser.setImageUrl(file.getBytes());

	            // Save the file to the file system if needed (you may want to check if the directory exists)
	            File saveFile = new ClassPathResource("static/image").getFile();
	            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	            System.out.println("Image uploaded successfully");
	           
	        }
			myuser.setRole("ROLE_USER");
			myuser.setEnable(true);
			
			myuser.setPassword(passwordEncoder.encode(myuser.getPassword()));
			System.out.println("agreement:"+ agreement);
			System.out.println("myuser:"+ myuser);
			myUser result=this.userRepository.save(myuser);
			
			System.out.println(result);
			model.addAttribute("myuser", new myUser());

			session.setAttribute("message",new Message("successfully registered", "alert-success") );
			return"signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("myuser", myuser);
			session.setAttribute("message", new Message("something went wrong!!"+ e.getMessage() ,"alert-error"));
			return "signup";
		}
       
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
	  
}
