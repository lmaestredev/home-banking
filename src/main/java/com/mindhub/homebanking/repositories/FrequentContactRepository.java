package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.FrequentContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrequentContactRepository extends JpaRepository<FrequentContact, Long> {
    FrequentContact findByAccountNumber(String accountNumber);
}
