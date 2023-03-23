package com.mindhub.Homebanking.Services.Implementations;

import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import com.mindhub.Homebanking.Services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImplementations implements ClientServices {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }
}
