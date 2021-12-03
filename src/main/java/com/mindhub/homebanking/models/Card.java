package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    private String cardHolder;
    private CardColor cardColor;
    private CardType cardType;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private String delete;

    public Card() { }

    public Card(CardType cardType, CardColor cardColor, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, String delete, Client client){
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.delete = delete;
        this.client = client;
        this.cardHolder = client.getFirstName() + " " + client.getLastName();
    }

    public Long               getId() { return id; }
    @JsonIgnore public Client getClient() { return client; }
    public CardColor          getCardColor() { return cardColor; }
    public CardType           getCardType() { return cardType; }
    public String             getNumber() { return number; }
    public int                getCvv() {  return cvv; }
    public LocalDateTime      getFromDate() { return fromDate; }
    public LocalDateTime      getThruDate() { return thruDate; }
    public String             getCardHolder() { return cardHolder;}
    public String             getDelete() {  return delete; }

    public void setDelete(String delete) { this.delete = delete; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
    public void setClient(Client client) { this.client = client; }
    public void setCardColor(CardColor cardColor) { this.cardColor = cardColor; }
    public void setCardType(CardType cardType) { this.cardType = cardType; }
    public void setNumber(String number) { this.number = number;  }
    public void setCvv(int cvv) { this.cvv = cvv; }
    public void setFromDate(LocalDateTime fromDate) { this.fromDate = fromDate;}
    public void setThruDate(LocalDateTime thruDate) { this.thruDate = thruDate;}
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardHolder=" + client +
                ", cardColor=" + cardColor +
                ", cardType=" + cardType +
                ", number='" + number + '\'' +
                ", cvv=" + cvv +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                '}';
    }
}


