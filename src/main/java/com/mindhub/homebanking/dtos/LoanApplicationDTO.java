package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanApplicationDTO {

    private String loanName;
    private double amount;
    private Integer payments;
    private String destinationAccountNumber;

    public LoanApplicationDTO() {}

    public LoanApplicationDTO( String loanName, double amount,  Integer payments, String destinationAccountNumber) {
        this.loanName = loanName;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccountNumber = destinationAccountNumber;
    }


    public String getLoanName() { return loanName; }
    public double getAmount() { return amount; }
    public Integer getPayments() { return payments; }
    public String getDestinationAccountNumber() { return destinationAccountNumber;}

    public void setLoanName(String loanName) { this.loanName = loanName; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPayments(Integer payments) { this.payments = payments; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
}
