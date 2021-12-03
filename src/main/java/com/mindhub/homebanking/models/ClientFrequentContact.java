package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientFrequentContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="frequentContact_id")
    private FrequentContact frequentContact;

    private ContactType contactType;

    public ClientFrequentContact() { }

    public ClientFrequentContact(ContactType contactType) {
        this.contactType = contactType;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public FrequentContact getFrequentContact() {
        return frequentContact;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public void setFrequentContact(FrequentContact frequentContact) {
        this.frequentContact = frequentContact;
    }

    @Override
    public String toString() {
        return "ClientFrequentContact{" +
                "id=" + id +
                ", client=" + client +
                ", frequentContact=" + frequentContact +
                ", contactType=" + contactType +
                '}';
    }
}
