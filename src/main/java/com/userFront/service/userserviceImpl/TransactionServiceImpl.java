package com.userFront.service.userserviceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userFront.dao.PrimaryAccountDao;
import com.userFront.dao.PrimaryTransactionDao;
import com.userFront.dao.RicipientDao;
import com.userFront.dao.SavingAccountDao;
import com.userFront.dao.SavingsTransactionDao;
import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.PrimaryTransaction;
import com.userFront.domain.Ricipient;
import com.userFront.domain.SavingAccount;
import com.userFront.domain.SavingsTransaction;
import com.userFront.domain.User;
import com.userFront.service.TransactionService;
import com.userFront.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;
	
	@Autowired
	private SavingsTransactionDao savingsTransactionDao;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingAccountDao savingsAccountDao;
	
	@Autowired
	private RicipientDao recipientDao;
	
	
	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		
		User user=userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactionList=user.getPrimaryAccount().getPrimaryTransaction();
		
		return primaryTransactionList;
	}

	@Override
	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		
		User user=userService.findByUsername(username);
		List<SavingsTransaction> savingsTransactionList=user.getSavingAccount().getSavingsTransaction();
		
		
		return savingsTransactionList;
	}

	@Override
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		
		primaryTransactionDao.save(primaryTransaction);
		
	}

	@Override
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		
		savingsTransactionDao.save(savingsTransaction);
		
	}

	@Override
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		
		primaryTransactionDao.save(primaryTransaction);
	}

	@Override
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
		
	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingAccount savingsAccount) throws Exception {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Account", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
        } else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
        } else {
            throw new Exception("Invalid Transfer");
        }
		
	}

	@Override
	public List<Ricipient> findRicipientList(Principal principal) {
		String username = principal.getName();
        List<Ricipient> recipientList = recipientDao.findAll().stream() 			//convert list to stream
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	
                .collect(Collectors.toList());

      //filters the line, equals to username
        return recipientList;
	}

	@Override
	public Ricipient saveRicipient(Ricipient recipient) {
		
		return recipientDao.save(recipient);
	}

	@Override
	public Ricipient findRicipientByName(String recipientName) {
		
		return recipientDao.findByName(recipientName);
	}

	@Override
	public void deleteRicipientByName(String recipientName) {
		
		recipientDao.deleteByName(recipientName); ;
	}

	@Override
	public void toSomeoneElseTransfer(Ricipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingAccount savingsAccount) {
		if (accountType.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
        } else if (accountType.equalsIgnoreCase("Savings")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
        }
		
	}

}
