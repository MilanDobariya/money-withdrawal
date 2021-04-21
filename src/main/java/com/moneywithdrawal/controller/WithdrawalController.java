package com.moneywithdrawal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moneywithdrawal.model.User;
import com.moneywithdrawal.model.Withdrawn;
import com.moneywithdrawal.service.WithdrawalService;

@Controller
@RequestMapping("/withdrawal")
public class WithdrawalController {
	private String prefix = "withdrawal";
	
	@Autowired
	private WithdrawalService withdrawalService;
	
	@RequestMapping("/form")
	public String getWithdrawalForm(Model model) {
		Withdrawn withdrawn = new Withdrawn();
		withdrawn.setCurrencyType("INR");
		model.addAttribute("withdrawal", withdrawn);
		return prefix+ "/add";
	}
	
	@RequestMapping("/internationalForm")
	public String internationalForm(Model model) {
		model.addAttribute("withdrawal", new Withdrawn());
		return prefix+ "/internationalForm";
	}
	
	@PostMapping("/save")
	public String saveWithdrawal(@ModelAttribute("withdrawal")Withdrawn withdrawal, HttpSession session,Model model) {
		String currencyType = withdrawal.getCurrencyType(); 
		if(!currencyType.equals("INR")) {
			withdrawal = withdrawalService.getInternationalWithdrawal(withdrawal);
		}
		if(!validateTransactionLimit(session,withdrawal)) {
			model.addAttribute("errorMsg", "Your Monthly withdrawal limit is not allow more then 50% of salary");
			model.addAttribute("withdrawal", withdrawal);
			return currencyType.equals("INR") ? prefix+"/add" : prefix+"/internationalForm";
		}
		if(currencyType.equals("INR")) {
			if(withdrawal.getAmount() % 100 != 0) {
				model.addAttribute("errorMsg", "Withdrawal amount should be multiple of 100");
				model.addAttribute("withdrawal", withdrawal);
				return prefix+"/add";
			}
			if(withdrawal.getAmount() % withdrawal.getNoteType()!=0) {
				model.addAttribute("errorMsg", "Please select notes as per amount");
				model.addAttribute("withdrawal", withdrawal);
				return prefix+"/add";
			}
			withdrawal.setQuantity((int) (withdrawal.getAmount()/withdrawal.getNoteType()));
		}
		boolean isFirstWithdrawal = false;
		
		List<Withdrawn>  withdrawns = (List<Withdrawn>) session.getAttribute("withdrawals");
		if(withdrawns == null || withdrawns.size() == 0) {
			withdrawns = new ArrayList<>();
			isFirstWithdrawal = true;
		}else {
			withdrawal.setCharge(5);
		}
		withdrawns.add(withdrawal);
		session.setAttribute("withdrawals", withdrawns);
		model.addAttribute("withdrawal", withdrawal);
		updateUser(isFirstWithdrawal,withdrawal,session);
		return prefix+"/success";
	}
	
	@RequestMapping("/transactionList")
	public String getTransactionList(Model model,HttpSession session) {
		List<Withdrawn>  withdrawns = (List<Withdrawn>) session.getAttribute("withdrawals");
		model.addAttribute("withdrawals", withdrawns);
		model.addAttribute("user", session.getAttribute("user"));
		return "balance/transactionList";
	}
	
	private String updateUser(boolean isFirstWithdrawal,Withdrawn withdrawal,HttpSession session) {
		User user = (User) session.getAttribute("user");
		user.setBalance(user.getBalance()-withdrawal.getAmount()-withdrawal.getCharge());
		session.setAttribute("user", user);
		return "";
	}
	
	private boolean validateTransactionLimit(HttpSession session,Withdrawn withdrawal) {
		List<Withdrawn>  withdrawns = (List<Withdrawn>) session.getAttribute("withdrawals");
		User user = (User) session.getAttribute("user");
		int totalAmount = (int) withdrawal.getAmount();
		if(withdrawns != null && withdrawns.size()>0)
			for(Withdrawn obj : withdrawns) {
				totalAmount += obj.getAmount()+obj.getCharge();
			}
		System.out.println("totalAmount => "+totalAmount);
		return user.getSalary()/2 >= totalAmount ? true : false;
	}
}
