package com.userFront.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class SavingAccount {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private int accountNumber;
	private BigDecimal accountBalance;
	
	@OneToMany(mappedBy="savingAccount",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	private List<SavingsTransaction> savingsTransaction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public List<SavingsTransaction> getSavingsTransaction() {
		return savingsTransaction;
	}

	public void setSavingsTransaction(List<SavingsTransaction> savingsTransaction) {
		this.savingsTransaction = savingsTransaction;
	}

	@Override
	public String toString() {
		return "SavingAccount [id=" + id + ", accountNumber=" + accountNumber + ", accountBalance=" + accountBalance
				+ "]";
	}
	
	
	
}
