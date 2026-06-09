package com.hotel.service;
import com.hotel.model.Customer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static final CustomerService INSTANCE = new CustomerService();

    // Map facilitates fast unique lookup by Email string
    private final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() {}

    public static CustomerService getInstance() {
        return INSTANCE;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
