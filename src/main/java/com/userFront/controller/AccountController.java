package com.userFront.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.PrimaryTransaction;
import com.userFront.domain.SavingAccount;
import com.userFront.domain.SavingsTransaction;
import com.userFront.domain.User;
import com.userFront.service.AccountService;
import com.userFront.service.TransactionService;
import com.userFront.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private  TransactionService transactionService;
	
	@RequestMapping("/primaryAccount")
	public String primaryAccount(Model model,Principal principle)
	{
		
		List<PrimaryTransaction>  primaryTransactionList=transactionService.findPrimaryTransactionList(principle.getName());
	
		User user=userService.findByUsername(principle.getName());
		PrimaryAccount primaryAccount=user.getPrimaryAccount();
		model.addAttribute("primaryAccount",primaryAccount);
		model.addAttribute("primaryTransactionList",primaryTransactionList);
		
		return "primaryAccount";
	}
	@RequestMapping("/savingsAccount")
	public String savingAccount(Model model,Principal principle)
	{
		
		List<SavingsTransaction>  savingsTransactionList=transactionService.findSavingsTransactionList(principle.getName());
		
		User user=userService.findByUsername(principle.getName());
		SavingAccount savingsAccount=user.getSavingAccount();
		model.addAttribute("savingsAccount",savingsAccount);
		model.addAttribute("savingsTransactionList",savingsTransactionList);
		
		return "savingsAccount";
	}

	@GetMapping("/deposit")
	public String deposite(Model model)
	{
		model.addAttribute("accountType","");
		model.addAttribute("amount","");
		
		return "deposit";
	}
	
	@PostMapping("/deposit")
	public String depositePOST(@ModelAttribute("amount") String amount,@ModelAttribute("accountType") String accountType,Principal principle)
	{
		accountService.deposit(accountType,Double.parseDouble(amount),principle);
		
		return "redirect:/userFront";
	}
	
	
	@GetMapping("/withdraw")
	public String withdraw(Model model)
	{
		model.addAttribute("accountType","");
		model.addAttribute("amount","");
		
		return "withdraw";
	}
	
	@PostMapping("/withdraw")
	public String withdrowPOST(@ModelAttribute("amount") String amount,@ModelAttribute("accountType") String accountType,Principal principle)
	{
		accountService.withdraw(accountType,Double.parseDouble(amount),principle);
		
		return "redirect:/userFront";
	}
	
}
