package com.springproj.demo.Repository;

import com.springproj.demo.Model.Account;
import com.springproj.demo.Model.Customer;
import com.springproj.demo.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

@Repository
public class JdbcCustomerRepository implements CustomerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcCustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertToCustomerTable(Customer customer) {
        return jdbcTemplate.update("INSERT INTO customers (customer_id, customer_name ) VALUES(?,?)",
                customer.getCustomerId(), customer.getCustomerName());

    }
    @Override
    public int insertToAccountTable(Account account) {
        return jdbcTemplate.update("INSERT INTO accounts (account_number, customer_id) VALUES(?,?)",
                account.getAccountNumber(), account.getCustomer().getCustomerId());

    }

    @Override
    public int insertToTransactionTable(Transaction transaction) {
        return jdbcTemplate.update("INSERT INTO transactions (account_number, trx_amount, description, trx_date, trx_time) VALUES(?,?,?,?,?)",
                transaction.getAccountNumber(), transaction.getTrxAmount(), transaction.getDescription(), transaction.getTrxDate(), transaction.getTrxTime());

    }

    @Override
    public int updateDescription(Transaction transaction, String id){
        return jdbcTemplate.update("UPDATE transactions SET description=? WHERE transaction_id=?",
                transaction.getDescription(), id);

    }

    @Override
    public Page<Transaction> findTransactions(String customerID, String accountNum, String description, Pageable pageable) {
        String sql = "SELECT t.transaction_id, t.account_number, t.trx_amount, t.description, t.trx_date, t.trx_time "
                + "FROM transactions t "
                + "INNER JOIN accounts a ON t.account_number = a.account_number "
                + "INNER JOIN customers c ON a.customer_id = c.customer_id "
                + "WHERE (c.customer_id = COALESCE(?, c.customer_id)) "
                + "OR (a.account_number = COALESCE(?, a.account_number)) "
                + "OR (t.description LIKE ?) "
                + "ORDER BY t.transaction_id "
                + "LIMIT ? OFFSET ?";
        Object[] params = new Object[5];
        params[0] = customerID;
        params[1] = accountNum;
        params[2] = "'%" + description + "%'";
        params[3] = pageable.getPageSize();
        params[4] = pageable.getOffset();
        System.out.print(params[2]);
        List<Transaction> transactions = jdbcTemplate.query(sql, params, new RowMapper<Transaction>() {
            @Override
            public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
                Transaction transaction;
                String accountNumber = rs.getString("account_number");
                BigDecimal trxAmount = rs.getBigDecimal("trx_amount");
                String trxDescription = rs.getString("description");
                Date trxDate = rs.getDate("trx_date");
                Time trxTime = rs.getTime("trx_time");

                if (customerID == null){
//                    if (trxDescription.toLowerCase().contains(description.toLowerCase())){
                            Customer customer = getCustomerByAccountNumber(accountNumber);
                            Account account = new Account(accountNumber,customer);
                            transaction = new Transaction(account, trxAmount, trxDescription, trxDate, trxTime);
//                    }
//                    System.out.println(transaction);
                } else{
                    Customer customer = getCustomerById(Integer.parseInt(customerID));
                    Account account = new Account(accountNumber, customer);
                    transaction = new Transaction(account, trxAmount, trxDescription, trxDate, trxTime);
//                    System.out.print(transaction);
                }
                return transaction;
            }
        });

        long totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (" + sql + ") AS count_query", Long.class, params);

        return new PageImpl<>(transactions, pageable, totalCount);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        Object[] params = new Object[]{customerId};
        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Customer.class));
    }

    @Override
    public Customer getCustomerByAccountNumber(String accountNumber){
        String sql = "SELECT c.* FROM customers c INNER JOIN accounts a ON c.customer_id = a.customer_id WHERE a.account_number = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] { accountNumber }, new RowMapper<Customer>() {
            @Override
            public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                return customer;
            }
        });
    }

    @Override
    public Account getAccountByAccountNumber(String accountNum) {
        String sql = "SELECT * FROM accounts a INNER JOIN customers c ON a.customer_id = c.customer_id WHERE account_number = ?";
        Object[] params = new Object[2];
        params[0] = accountNum;
//        params[1] = customer;
        Account account = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Account.class));
        return account;
    }

    @Override
    public Transaction findById(String id) {
        try {
            Transaction transaction = jdbcTemplate.queryForObject("SELECT * FROM transactions WHERE transaction_id=?",
                    BeanPropertyRowMapper.newInstance(Transaction.class), id);
            return transaction;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }


    @Override
    public boolean checkCustomerExist(Customer customer){
        String query = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, customer.getCustomerId());
        if (count > 0) {
            return true;
        }
        return false;
    }
    @Override
    public boolean checkAccountExist(Account account){
        String query = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, account.getAccountNumber());
        if (count > 0) {
            return true;
        }
        return false;
    }


}
