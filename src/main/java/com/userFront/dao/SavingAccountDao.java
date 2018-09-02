package com.userFront.dao;

import org.springframework.data.repository.CrudRepository;

import com.userFront.domain.SavingAccount;

public interface SavingAccountDao extends CrudRepository<SavingAccount, Long> {


	SavingAccount findByAccountNumber(int accountNumber);

}
