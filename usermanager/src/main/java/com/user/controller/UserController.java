package com.user.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.user.helper.Message;
import com.user.model.Address;
import com.user.model.User;
import com.user.repository.AddressRepository;
import com.user.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
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
	
	//add address form processing
	@PostMapping("/process-address")
	public String processAddress(@ModelAttribute Address address, Principal principal, HttpSession session) {
		
		try {
		String name = principal.getName();
		
		User user = this.userRepository.getUserByUserName(name);
		
		address.setUser(user);
		user.getAddress().add(address);
		this.userRepository.save(user);
		
		System.out.println("DATA " +address);
		System.out.println("Address Saved For User");
		
		session.setAttribute("message", new Message("Your Address Is Added", "success"));
		
		}catch (Exception e) {
			
			System.out.println("ERROR" +e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("ERROR", "danger"));
		}
		
		return "normal/add_address_form";
	}

	//display address
	
	@GetMapping("/display-address")
	public String displayAddress(Model model, Principal principal) {
		model.addAttribute("title", "Display Address");
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		List<Address> address = this.addressRepository.findAddressByUser(user.getId());
		
		model.addAttribute("address", address);
		
		return "normal/display_address";
	}
	
	// Handler For Displaying Address Indiviually
	
	@RequestMapping("{a_id}/address")
	public String displayAddressDetails(@PathVariable("a_id") Integer a_id, Model model, Principal principal) {
		
		System.out.println("Address ID "+a_id);
		
		Optional<Address> addressOptional = this.addressRepository.findById(a_id);
		Address address = addressOptional.get();
		
		//Security Check
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==address.getUser().getId())
		{
		model.addAttribute("address", address);
		model.addAttribute("title", address.getHouse_no());
		}
		return "normal/address_details";
	}
	
	//Handler For Deleting Address
	
	@GetMapping("/delete/{a_id}")
	public String deleteAddress(@PathVariable("a_id") Integer a_id, Model model, HttpSession session)
	{
		
		Optional <Address> addressOptional = this.addressRepository.findById(a_id);
		Address address = addressOptional.get();
		
		System.out.println("Address" +address.getA_id());
		
		address.setUser(null);
		
		this.addressRepository.delete(address);
		
		System.out.println("Deleted");
		session.setAttribute("message", new Message("Address Deleted Successfully", "success"));	
		
		return "redirect:/user/display-address";
	}
	
	//Handler For Updating Address Form
	
	@PostMapping("/update-address/{a_id}")
	public String updateAddress(@PathVariable("a_id") Integer a_id, Model model)
	{
		
		model.addAttribute("title", "Update Address");
		
		Address address = this.addressRepository.findById(a_id).get();	
		model.addAttribute("address", address);
		
		return "normal/update_address";
	}
	
	//Update Handler
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Address address, HttpSession session, Model model, Principal principal)
	{
		
		try {
			
			User user = this.userRepository.getUserByUserName(principal.getName());
			address.setUser(user);
			
			this.addressRepository.save(address);
			
			session.setAttribute("message", new Message("Your Address Is Updated", "success"));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Address" +address.getHouse_no());
		
		return "redirect:/user/" +address.getA_id()+"address";
		
	}
	
	
}
