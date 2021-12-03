package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.ClientFrequentContact;
import com.mindhub.homebanking.models.FrequentContact;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event;

public interface ClientFrequentContactRepository extends JpaRepository<ClientFrequentContact, Long> {
    ClientFrequentContact findById(ID id);

}
