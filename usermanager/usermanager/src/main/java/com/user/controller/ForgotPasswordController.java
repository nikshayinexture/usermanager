package com.user.controller;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {
	
	Random random = new Random(1000);
	
	//Email ID Form Open Handler
	
	@RequestMapping("/forgot")
	public String forgotPassword()
	{
		return "forgot_password";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email)
	{
		System.out.println("EMAIL" +email);
		
		//Generate OTP
		
		int otp = random.nextInt(9999);
		
		System.out.println("OTP -" +otp);
		
		//Sending OTP To Email
		
		
		
		return "verify-otp";
	}
	
}

