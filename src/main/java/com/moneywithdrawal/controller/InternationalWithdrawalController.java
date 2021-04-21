package com.moneywithdrawal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moneywithdrawal.model.Withdrawn;

@Controller
public class InternationalWithdrawalController {
	private String prefix = "internationalWithdrawal";
	
	@RequestMapping("/form")
	public String getWithdrawalForm(Model model) {
		model.addAttribute("withdrawal", new Withdrawn());
		return prefix+ "/add";
	}
}
