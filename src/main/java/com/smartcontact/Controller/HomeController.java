package com.smartcontact.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	 public String registerUser(@Valid @ModelAttribute("myuser") myUser myuser,BindingResult result2,@RequestParam(value="agreement",defaultValue ="false") boolean agreement ,Model model,HttpSession session ) { // Perform user
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
			
			myuser.setRole("ROLE_USER");
			myuser.setEnable(true);
			myuser.setImageurl("defult.png");
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
	 

	  
}
