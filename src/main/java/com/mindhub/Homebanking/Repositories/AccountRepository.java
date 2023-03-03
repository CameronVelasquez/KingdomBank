package com.mindhub.Homebanking.Repositories;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {


    Boolean existsByNumber(String number);
    Account findByNumber(String number);
}
