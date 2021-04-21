package com.moneywithdrawal.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moneywithdrawal.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	private String prefix = "user";
	
	@RequestMapping("/registration")
	public String index(Model model) {
		model.addAttribute("user", new User());
		return prefix+"/registration";
	}
	
	@PostMapping("/save")
	public String saveUser(@ModelAttribute("user")User user, HttpSession session,Model model) {
		user.setAccountNumber(randomNumber(12));
		user.setBalance(user.getBalance()+user.getSalary());
		session.setAttribute("user", user);
		model.addAttribute("user", user);
		session.removeAttribute("withdrawals");
		return prefix+"/registrationSuccess";
	}
	
	public String randomNumber(int size) {
		String string = "0123456789";
		String number = "";
		for (int i = 0; i < size; i++) {
			int index = (int) (string.length() * Math.random());
			while (i == 0 && string.charAt(index) == '0')
				index = (int) (string.length() * Math.random());
			number = number + string.charAt(index);
		}
		return number.trim();
	}
}
