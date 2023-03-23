package com.mindhub.Homebanking.Utils;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Loan;
import com.mindhub.Homebanking.Services.AccountServices;
import com.mindhub.Homebanking.Services.CardServices;

public class Utilities {

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
    public static   String randomNumberCard(CardServices cardServices){
        String generatedNumber;
        generatedNumber = randomString();
        if(cardServices.existCardByNumber(generatedNumber) ){
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
    public static String accountNumber(AccountServices accountServices){

        String Number;
        boolean verifyNumber;
        do {
            Number = GenerateNumber();
            verifyNumber = accountServices.existByNumber(Number);
        } while (verifyNumber);

        return Number;
    }
    public static Double loanFees(Double amount, Loan loan){
        Double fee = loan.getFee();
        Double amountPlusFees = amount * fee;
        return amountPlusFees;
    }
    public static Double currentBalanceCredit(AccountServices accountServices, Account account, double amount){
        Double getCurrentAmount = accountServices.findByNumber(account.getNumber()).getBalance() + amount;

        return getCurrentAmount;

    }
    public static Double currentBalanceDebit(AccountServices accountServices, Account account, double amount){
        Double getCurrentAmount = accountServices.findByNumber(account.getNumber()).getBalance() - amount;

        return getCurrentAmount;

    }
}
