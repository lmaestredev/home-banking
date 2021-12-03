package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {

    private Long id;
    private CardColor cardColor;
    private CardType cardType;
    private String number;
    private int cvv;
    private String cardHolder;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private String delete;

    public CardDTO() { }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardColor = card.getCardColor();
        this.cardType = card.getCardType();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cardHolder = card.getCardHolder();
        this.delete = card.getDelete();
    }

    public Long getId() { return id; }
    public CardColor getCardColor() { return cardColor;  }
    public CardType getCardType() {  return cardType; }
    public String getNumber() { return number;}
    public int getCvv() { return cvv; }
    public LocalDateTime getFromDate() { return fromDate;  }
    public LocalDateTime getThruDate() { return thruDate; }
    public String getCardHolder() { return cardHolder; }
    public String getDelete() { return delete; }

    public void setDelete(String delete) {  this.delete = delete; }
    public void setId(Long id) { this.id = id;  }
    public void setCardColor(CardColor cardColor) {  this.cardColor = cardColor; }
    public void setCardType(CardType cardType) { this.cardType = cardType;  }
    public void setNumber(String number) { this.number = number; }
    public void setCvv(int cvv) { this.cvv = cvv; }
    public void setFromDate(LocalDateTime fromDate) { this.fromDate = fromDate; }
    public void setThruDate(LocalDateTime thruDate) { this.thruDate = thruDate; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }

    @Override
    public String toString() {
        return "CardDTO{" +
                "id=" + id +
                ", cardColor=" + cardColor +
                ", cardType=" + cardType +
                ", number='" + number + '\'' +
                ", cvv=" + cvv +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                ", cardHolder='" + cardHolder + '\'' +
                '}';
    }
}

