package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createNewCard(Authentication authentication, @RequestParam String cardType, @RequestParam String cardColor){

        int maxNumberCard = 9999;
        int minNumberCard = 1000;

        int maxNumberCvv = 999;
        int minNumberCvv = 100;

        int blockOne = (int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard;
        int blockTwo = (int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard;
        int blockThree = (int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard;
        int blockFour = (int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard;

        int cvv = (int) (Math.random() * (maxNumberCvv - minNumberCvv + 1)) + minNumberCvv;

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Set<Card> clientCards = authenticatedClient.getCards().stream().filter(card -> card.getDelete().contains("no")).collect(Collectors.toSet());
        Set<Card> debitCardsSet = clientCards.stream().filter(card -> card.getCardType().equals(CardType.DEBIT)).collect(Collectors.toSet());
        Set<Card> creditCardsSet = clientCards.stream().filter(card -> card.getCardType().equals(CardType.CREDIT)).collect(Collectors.toSet());

        if(cardType.isEmpty() || cardColor.isEmpty() ){
            return new ResponseEntity<>("Complete all the fields, please", HttpStatus.FORBIDDEN);
        }

        if (clientCards.size() == 6){
            return new ResponseEntity<>("you already have the number of cards allowed", HttpStatus.FORBIDDEN);
        }

        if(debitCardsSet.size() == 3 && cardType.equals("DEBIT")){
            return new ResponseEntity<>("you already have the number of debit cards allowed", HttpStatus.FORBIDDEN);
        }

        if(creditCardsSet.size() == 3  && cardType.equals("CREDIT")){
            return new ResponseEntity<>("you already have the number of credit cards allowed", HttpStatus.FORBIDDEN);
        }

        if (debitCardsSet.stream().filter(card -> card.getCardColor().equals(CardColor.valueOf(cardColor))).collect(Collectors.toSet()).size() > 0 && cardType.equals("DEBIT")){
            return new ResponseEntity<>("you already have a debit card with this color", HttpStatus.FORBIDDEN);
        }

        if (creditCardsSet.stream().filter(card -> card.getCardColor().equals(CardColor.valueOf(cardColor))).collect(Collectors.toSet()).size() > 0 && cardType.equals("CREDIT")){
            return new ResponseEntity<>("you already have a credit card with this color", HttpStatus.FORBIDDEN);
        }

        if(cardType.equals("CREDIT") || cardType.equals("DEBIT") || cardColor.equals("GOLD") || cardColor.equals("SILVER") || cardColor.equals("TITANIUM") ){
            cardRepository.save( new Card(CardType.valueOf(cardType), CardColor.valueOf(cardColor), blockOne+"-"+blockTwo+"-"+blockThree+"-"+blockFour, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5),"no", authenticatedClient));
            return new ResponseEntity<>("New card created successfully", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Data signed in, is not correct", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/api/deleteCard/{cardNumber}")
    public ResponseEntity<?> deleteCard(Authentication authentication, @PathVariable String cardNumber){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Card cardToDelete = cardRepository.findByNumber(cardNumber);

        if(cardNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (authenticatedClient.getCards().stream().filter(card -> card.getNumber().equals(cardNumber)).collect(Collectors.toSet()).size() == 0){
            return new ResponseEntity<>("Card to delete is not yours", HttpStatus.FORBIDDEN);
        }

        cardToDelete.setDelete("yes");
        cardRepository.save(cardToDelete);
        return new ResponseEntity<>("Card deleted successfully", HttpStatus.CREATED);
    }
}
