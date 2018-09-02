package com.userFront.controller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.Ricipient;
import com.userFront.domain.SavingAccount;
import com.userFront.domain.User;
import com.userFront.service.TransactionService;
import com.userFront.service.UserService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	    @Autowired
	    private TransactionService transactionService;

	    @Autowired
	    private UserService userService;
	    
	    @GetMapping("/betweenAccounts")
	    public String betweenAccounts(Model model) {
	        model.addAttribute("transferFrom", "");
	        model.addAttribute("transferTo", "");
	        model.addAttribute("amount", "");

	        return "betweenAccounts";
	    }

	    @PostMapping("/betweenAccounts")
	    public String betweenAccountsPost(
	            @ModelAttribute("transferFrom") String transferFrom,
	            @ModelAttribute("transferTo") String transferTo,
	            @ModelAttribute("amount") String amount,
	            Principal principal
	    ) throws Exception {
	        User user = userService.findByUsername(principal.getName());
	        PrimaryAccount primaryAccount = user.getPrimaryAccount();
	        SavingAccount savingsAccount = user.getSavingAccount();
	        transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);

	        return "redirect:/userFront";
	    }
	    
	    @GetMapping("/recipient")
	    public String recipient(Model model, Principal principal) {
	        List<Ricipient> recipientList = transactionService.findRicipientList(principal);

	        Ricipient recipient = new Ricipient();

	        model.addAttribute("recipientList", recipientList);
	        model.addAttribute("recipient", recipient);

	        return "recipient";
	    }

	    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
	    public String recipientPost(@ModelAttribute("recipient") Ricipient recipient, Principal principal) {

	        User user = userService.findByUsername(principal.getName());
	        recipient.setUser(user);
	        transactionService.saveRicipient(recipient);

	        return "redirect:/transfer/recipient";
	    }

	    @GetMapping("/recipient/edit")
	    public String recipientEdit(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

	        Ricipient recipient = transactionService.findRicipientByName(recipientName);
	        List<Ricipient> recipientList = transactionService.findRicipientList(principal);

	        model.addAttribute("recipientList", recipientList);
	        model.addAttribute("recipient", recipient);

	        return "recipient";
	    }

	    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
	    @Transactional
	    public String recipientDelete(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

	        transactionService.deleteRicipientByName(recipientName);

	        List<Ricipient> recipientList = transactionService.findRicipientList(principal);

	        Ricipient recipient = new Ricipient();
	        model.addAttribute("recipient", recipient);
	        model.addAttribute("recipientList", recipientList);


	        return "recipient";
	    }

	    @GetMapping("/toSomeoneElse")
	    public String toSomeoneElse(Model model, Principal principal) {
	        List<Ricipient> recipientList = transactionService.findRicipientList(principal);

	        model.addAttribute("recipientList", recipientList);
	        model.addAttribute("accountType", "");

	        return "toSomeoneElse";
	    }

	    @PostMapping("/toSomeoneElse")
	    public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName, @ModelAttribute("accountType") String accountType, @ModelAttribute("amount") String amount, Principal principal) {
	        User user = userService.findByUsername(principal.getName());
	        Ricipient recipient = transactionService.findRicipientByName(recipientName);
	        transactionService.toSomeoneElseTransfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingAccount());

	        return "redirect:/userFront";
	    }
	
}
