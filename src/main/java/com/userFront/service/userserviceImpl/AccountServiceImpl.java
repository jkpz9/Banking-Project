package com.userFront.service.userserviceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userFront.dao.PrimaryAccountDao;
import com.userFront.dao.SavingAccountDao;
import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.PrimaryTransaction;
import com.userFront.domain.SavingAccount;
import com.userFront.domain.SavingsTransaction;
import com.userFront.domain.User;
import com.userFront.service.AccountService;
import com.userFront.service.TransactionService;
import com.userFront.service.UserService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	private static int nextAccountNumber = 11223145;
	
	    @Autowired
	    private PrimaryAccountDao primaryAccountDao;

	    @Autowired
	    private SavingAccountDao savingAccountDao;
	    
	    @Autowired
        private UserService userService;
	    
	    @Autowired
	    private TransactionService transactionService;
	    

	@Override
	public PrimaryAccount createPrimaryAccount() {
		 PrimaryAccount primaryAccount = new PrimaryAccount();
	        primaryAccount.setAccountBalance(new BigDecimal(0.0));
	        primaryAccount.setAccountNumber(accountGen());

	        primaryAccountDao.save(primaryAccount);

	        return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	@Override
	public SavingAccount createSavingAccount() {
		SavingAccount savingAccount = new SavingAccount();
        savingAccount.setAccountBalance(new BigDecimal(0.0));
        savingAccount.setAccountNumber(accountGen());

        savingAccountDao.save(savingAccount);

        return savingAccountDao.findByAccountNumber(savingAccount.getAccountNumber());
	}

	 private int accountGen() {
	        return ++nextAccountNumber;
	    }

	@Override
	public void deposit(String accountType, double amount, Principal principle) {
		
		User user = userService.findByUsername(principle.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingAccount savingsAccount = user.getSavingAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
		
	}

	@Override
	public void withdraw(String accountType, double amount, Principal principle) {
		
		User user = userService.findByUsername(principle.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingAccount savingsAccount = user.getSavingAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
        }
		
	}

	
}
