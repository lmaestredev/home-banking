package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    private Integer payments;

    private double amount;

    public ClientLoan() {  }

    public ClientLoan(double amount, Integer payments) {
        this.amount = amount;
        this.payments = payments;
    }

    public long getId() { return id; }
    public Client getClient() { return client; }
    public Loan getLoan() {  return loan; }
    public Integer getPayments() { return payments; }
    public double getAmount() {  return amount; }

    public void setId(long id) { this.id = id; }
    public void setClient(Client client) { this.client = client; }
    public void setLoan(Loan loan) { this.loan = loan; }
    public void setPayments(Integer payments) {  this.payments = payments; }
    public void setAmount(double amount) { this.amount = amount; }

}
