package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.ArrayList;
import java.util.List;

public class LoanDTO {

    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();
    private double percentage;

    public LoanDTO() {}

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.percentage = loan.getPercentage();
    }

    public Long getId() { return id; }
    public String getName() {return name; }
    public double getMaxAmount() { return maxAmount; }
    public List<Integer> getPayments() {  return payments;  }
    public double getPercentage() {return percentage;}

    public void setPercentage(double percentage) {this.percentage = percentage;}
    public void setName(String name) { this.name = name; }
    public void setMaxAmount(double maxAmount) { this.maxAmount = maxAmount; }
    public void setPayments(List<Integer> payments) { this.payments = payments; }
}
