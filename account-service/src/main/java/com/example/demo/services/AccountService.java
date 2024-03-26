package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import com.example.demo.entities.BankAccount;

public interface AccountService {
	public BankAccount saveAccount(BankAccount account);

	public List<BankAccount> findAllAccounts();

	public Optional<BankAccount> findAccountById(Long id);

	public BankAccount updateAccount(BankAccount updatedAccount);

	public void deleteAccount(Long id);

	public List<BankAccount> findAccountByCustomerId(Long id);

	public void deleteAccountsByCustomerId(Long id);

}
