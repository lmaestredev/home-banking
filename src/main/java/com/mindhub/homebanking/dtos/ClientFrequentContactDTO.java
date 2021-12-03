package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientFrequentContact;
import com.mindhub.homebanking.models.ContactType;
import com.mindhub.homebanking.models.FrequentContact;

public class ClientFrequentContactDTO {
    private Long id;
    private Long frequentContactId;
    private String fullName;
    private String accountNumber;
    private ContactType contactType;

    public ClientFrequentContactDTO() {
    }

    public ClientFrequentContactDTO(ClientFrequentContact clientFrequentContact) {
        this.id = clientFrequentContact.getId();
        this.frequentContactId = clientFrequentContact.getFrequentContact().getId();
        this.fullName = clientFrequentContact.getFrequentContact().getFirstName() +" "+ clientFrequentContact.getFrequentContact().getLastName();
        this.accountNumber = clientFrequentContact.getFrequentContact().getNumber();
        this.contactType = clientFrequentContact.getContactType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public Long getFrequentContactId() {
        return frequentContactId;
    }

    public void setFrequentContactId(Long frequentContactId) {
        this.frequentContactId = frequentContactId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
