package com.springproj.demo.Model;

/**
 *  Customer class.
 */
public class Customer {
    private int customerId;
    private String customerName;
    /**
     * Instantiates a new Customer.
     */
    public Customer(){

    }

    /**
     * Instantiates a new Customer.
     *
     * @param customerId  the customer id
     * @param customerName name of the customer (Nullable)
     */
    public Customer(int  customerId, String customerName){
        this.customerId = customerId;
        this.customerName = customerName;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets customer id.
     *
     * @param customerId the customer id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * The getCustomerName function returns the name of the customer.
     *
     * @return The customername variable
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * The setCustomerName function sets the customerName variable to a new value.
     *
     * @param String customerName Set the customername variable
     *
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets account num of the customer.
     *
     * @return the account num of the customer
     */

    /**
     * The getCustomerFieldNames function returns an array of strings containing the names of the fields in a customer record.
     *
     * @return An array of strings, each string being the name of a field in the customer table
     */
    public static String[] getCustomerFieldNames() {
        return new String[] {"customerId", "customerName"};
    }

    /**
     * The toString function is used to print out the contents of a class.
     * It is called by using System.out.println(object);
     *
     * @return The customerid, accountnum, firstname, lastname and description
     */
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
