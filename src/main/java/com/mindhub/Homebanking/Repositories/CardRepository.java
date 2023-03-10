package com.mindhub.Homebanking.Repositories;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
     Boolean existsCardByNumber(String number);
     Card findByNumber(String number);
}
