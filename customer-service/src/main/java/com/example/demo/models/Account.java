package com.example.demo.models;

import java.time.LocalDate;

import com.example.demo.entities.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString

public class Account {

	private Long accountId;
	private double balance;
	private LocalDate createAt;
	private String currency;
	private String type;
	private Customer customer;
	private Long customerId;
}
