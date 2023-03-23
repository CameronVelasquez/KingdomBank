package com.mindhub.Homebanking.Services.Implementations;

import com.mindhub.Homebanking.Models.ClientLoan;
import com.mindhub.Homebanking.Repositories.ClientLoanRepository;
import com.mindhub.Homebanking.Services.ClientLoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServicesImplementations implements ClientLoanServices {
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Override
    public void save(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
