package com.springproj.demo.BatchJob;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import com.springproj.demo.Model.Account;
import com.springproj.demo.Model.Customer;
import com.springproj.demo.Model.Transaction;
import com.springproj.demo.Repository.JdbcCustomerRepository;
import org.springframework.stereotype.Component;
import org.apache.commons.beanutils.BeanUtils;

import static com.springproj.demo.Model.Account.getAccountFieldNames;
import static com.springproj.demo.Model.Customer.getCustomerFieldNames;
import static com.springproj.demo.Model.Transaction.getTransactionFieldNames;


/**
 * Customer batch job.
 */
@Component
public class CustomerBatchJob {
    private JdbcCustomerRepository customerRepo;

    /**
     * Instantiates a new Customer batch job.
     *
     * @param customerRepo the customer repo
     */
    public CustomerBatchJob(JdbcCustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    /**
     * Read text file and separate the records line by line and map them according to their field names
     * and inserts into the database
     *
     * @param fileName the file name
     */
    public void readTextFile(String fileName) {

        String line = null;
        int rowsAffected = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String[] header = bufferedReader.readLine().split("\\|");

            while((line = bufferedReader.readLine()) != null) {
                Customer customer = new Customer();
                Account account = new Account();
                Transaction transaction = new Transaction();
                String[] values = line.split("\\|");
                for (int i = 0; i < values.length; i++) {
                    if (Objects.equals(header[i], "ACCOUNT_NUMBER")){
                        String propertyName = getAccountFieldNames()[0];
                        String propertyValue = values[i];
                        BeanUtils.setProperty(account, propertyName, propertyValue);
                    }
                    else if(Objects.equals(header[i], "CUSTOMER_ID")){
                        String propertyName = getCustomerFieldNames()[0];
                        String propertyValue = values[i];
                        BeanUtils.setProperty(customer, propertyName, propertyValue);

                    }else{
                        String propertyName = getTransactionFieldNames()[i];
                        String propertyValue = values[i];
                        BeanUtils.setProperty(transaction, propertyName, propertyValue);
                        BeanUtils.setProperty(account, getAccountFieldNames()[1], customer);
                    }

                }
                BeanUtils.setProperty(transaction, getTransactionFieldNames()[0], account);


                if (!customerRepo.checkCustomerExist(customer)) {
                    customerRepo.insertToCustomerTable(customer);
                }else{
                    // do nothing
                }
                if (!customerRepo.checkAccountExist(account)){
                    customerRepo.insertToAccountTable(account);
                }else{
                    //do nothing
                }
                customerRepo.insertToTransactionTable(transaction);

            }

            bufferedReader.close();
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
