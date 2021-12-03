package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name;
    private double maxAmount;
    private double percentage;

    @ElementCollection
    @Column(name="payment")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments, double percentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }

    public Long getId() { return id; }
    public String getName() {  return name;}
    public double getMaxAmount() { return maxAmount;}
    public List<Integer> getPayments() { return payments; }
    public double getPercentage() {return percentage;}



    @JsonIgnore
    public List<Client> getClients() { return clientLoans.stream().map(sub -> sub.getClient()).collect(toList());}

    public void setId(Long id) { this.id = id;  }
    public void setName(String name) {  this.name = name; }
    public void setMaxAmount(double maxAmount) { this.maxAmount = maxAmount;}
    public void setPayments(List<Integer> payments) { this.payments = payments; }
    public void setPercentage(double percentage) {this.percentage = percentage;}

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

}
