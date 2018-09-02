package com.userFront.service;

import java.security.Principal;

import com.userFront.domain.PrimaryAccount;
import com.userFront.domain.SavingAccount;

public interface AccountService {

	PrimaryAccount createPrimaryAccount();

	SavingAccount createSavingAccount();

	public void deposit(String accountType, double parseDouble, Principal principle);

	public void withdraw(String accountType, double parseDouble, Principal principle);

}
