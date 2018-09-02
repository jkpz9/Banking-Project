package com.userFront.service;

import java.security.Principal;
import java.util.List;

import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.PrimaryTransaction;
import com.userFront.domain.Ricipient;
import com.userFront.domain.SavingAccount;
import com.userFront.domain.SavingsTransaction;

public interface TransactionService {
	
	List<PrimaryTransaction> findPrimaryTransactionList(String username);

    List<SavingsTransaction> findSavingsTransactionList(String username);

    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);

    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
    
    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);
    
    void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount) throws Exception;
    
   List<Ricipient> findRicipientList(Principal principal);

   Ricipient saveRicipient(Ricipient recipient);

   Ricipient findRicipientByName(String recipientName);

   void deleteRicipientByName(String recipientName);
    
    void toSomeoneElseTransfer(Ricipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingAccount savingsAccount);

}
