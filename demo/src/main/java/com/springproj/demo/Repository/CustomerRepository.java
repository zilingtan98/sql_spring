package com.springproj.demo.Repository;
import com.springproj.demo.Model.Account;
import com.springproj.demo.Model.Customer;
import com.springproj.demo.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRepository {

    int insertToCustomerTable(Customer customer);

    int insertToAccountTable(Account account);

    int insertToTransactionTable(Transaction transaction);

    int updateDescription(Transaction transaction, String id);

    Page<Transaction> findTransactions(String customerID, String accountNum, String description, Pageable pageable);

    Customer getCustomerById(int customerId);

    Customer getCustomerByAccountNumber(String accountNumber);

    Account getAccountByAccountNumber(String accountNum);

    Transaction findById(String id);

    boolean checkCustomerExist(Customer customer);

    boolean checkAccountExist(Account account);
}
