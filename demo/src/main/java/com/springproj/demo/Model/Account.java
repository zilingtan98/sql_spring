package com.springproj.demo.Model;

public class Account {
    private String accountNumber;
    private Customer customer;
    public Account(){}

    public Account(String accountNumber, Customer customer){
        this.accountNumber = accountNumber;
        this.customer = customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static String[] getAccountFieldNames() {
        return new String[] {"accountNumber", "customer" };
    }
    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", customer=" + customer +
                '}';
    }
}
