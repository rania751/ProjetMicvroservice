package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.*;
import com.example.demo.enums.AccountType;
import com.example.demo.models.Customer;

import java.time.LocalDate;
@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private double balance;
    private LocalDate createAt;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @Transient
    private Customer customer;
    private Long customerId;
}