package com.springproj.demo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *  Create sql table.
 */
@Component
public class CreateSQLTable {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Instantiates a new SQL table.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public CreateSQLTable(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * The createCustomersTable function creates a table in the database called customers.
     * The customer_id column is an integer and is set as the primary key, which means that it must be unique for each row.
     * The customer_name column can hold up to 50 characters of text.
     *
     */
    public void createCustomersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS customers ( " +
                "customer_id INT PRIMARY KEY, " +
                "customer_name VARCHAR(50) " +
                ")";
        jdbcTemplate.execute(sql);
    }

    public void createAccountsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_number VARCHAR(20) PRIMARY KEY,"
                + "customer_id INT,"
                + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)"
                + ")";
        jdbcTemplate.execute(sql);
    }
    public void createTransactionsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions ( " +
                "transaction_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "account_number VARCHAR(20), " +
                "trx_amount DECIMAL(12, 2), " +
                "description VARCHAR(50), " +
                "trx_date DATE, " +
                "trx_time TIME, " +
                "FOREIGN KEY (account_number) REFERENCES accounts(account_number) " +
                ")";
        jdbcTemplate.execute(sql);
    }

    public void createTablesAndRelationships() {
        createCustomersTable();
        createAccountsTable();
        createTransactionsTable();
    }
}

