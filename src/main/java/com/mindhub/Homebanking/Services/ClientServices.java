package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Models.Client;

import java.util.List;

public interface ClientServices {

    List<Client> findAll ();
    Client findById (Long id);
    Client findByEmail(String email);
    void save(Client client);

}
