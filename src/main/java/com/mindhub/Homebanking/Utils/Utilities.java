package com.mindhub.Homebanking.Utils;

import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.CardRepository;

public class Utilities {
    //preguntar mas tarde a Fede sobre el cardRepository
    public static String cvv() {
        int cvvGen = (int) (Math.random() * 999);
        String cvvGenCompletado = String.format("%03d", cvvGen);
        return cvvGenCompletado;
    }

    public static String randomString(){
        int cardNumber1 = (int) (Math.random() * 999);
        int cardNumber2 = (int) (Math.random() * 9999);
        int cardNumber3 = (int) (Math.random() * 9999);
        int cardNumber4 = (int) (Math.random() * 9999);
        String number1 = String.format("%03d", cardNumber1) ;
        String number2 = String.format("%04d", cardNumber2) ;
        String number3 = String.format("%04d", cardNumber3) ;
        String number4 = String.format("%04d", cardNumber4) ;
        String cardNumberString = "4" + number1+"-"+number2+"-"+number3+"-"+number4;
        return cardNumberString;
    }
    public static   String randomNumberCard(CardRepository cardRepository){
        String generatedNumber;
        generatedNumber = randomString();
        if(cardRepository.existsCardByNumber(generatedNumber) ){
            return generatedNumber = randomString();
        }
        else{
            return generatedNumber;
        }
    }

    public static String GenerateNumber(){
        int number1=(int) (Math.random() * (99999999));
        String number = "VIN-"+number1;//VIN-46877988
        return number;
    }
    public static String accountNumber(AccountRepository accountRepo){

        String Number;
        boolean verifyNumber;
        do {
            Number = GenerateNumber();
            verifyNumber = accountRepo.existsByNumber(Number);
        } while (verifyNumber);

        return Number;
    }
    public static Double loanFees(Double amount){
        Double amountPlusFees = amount * 0.2 + amount;
        return amountPlusFees;
    }
}
