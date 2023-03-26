package com.springproj.demo.Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class Transaction {

    private Account account;
    private BigDecimal trxAmount;
    private String description;
    private Date trxDate;
    private Time trxTime;

    public Transaction(){

    }
    public Transaction(Account account, BigDecimal trxAmount, String description, Date trxDate, Time trxTime) {
        this.account = account;
        this.trxAmount = trxAmount;
        this.description = description;
        this.trxDate = trxDate;
        this.trxTime = trxTime;
    }

    public Time getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(Time trxTime) {
        this.trxTime = trxTime;
    }

    public Date getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(Date trxDate) {
        this.trxDate = trxDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(BigDecimal trxAmount) {
        this.trxAmount = trxAmount;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountNumber() {
        return this.account.getAccountNumber();
    }

    public int getCustomerId() {
        return account.getCustomer().getCustomerId();
    }
    public static String[] getTransactionFieldNames() {
        return new String[] {"account", "trxAmount", "description", "trxDate", "trxTime" };
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "account=" + account +
                ", trxAmount=" + trxAmount +
                ", description='" + description + '\'' +
                ", trxDate=" + trxDate +
                ", trxTime=" + trxTime +
                '}';
    }
}
