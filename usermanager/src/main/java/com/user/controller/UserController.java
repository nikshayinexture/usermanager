package com.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.user.model.Address;
import com.user.model.User;
import com.user.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	//adding common data
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME" +userName);
		
		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER" +user);
		model.addAttribute("user", user);
	}
	
	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		model.addAttribute("ttile", "User Dashboard");
		return "normal/user_profile";
	}
	
	//Add Form Handler
	
	@GetMapping("/add-address")
	public String addAddressForm(Model model)
	{
		model.addAttribute("ttile", "Add Address");
		model.addAttribute("address", new Address());
		return "normal/add_address_form";
		
	}

}
