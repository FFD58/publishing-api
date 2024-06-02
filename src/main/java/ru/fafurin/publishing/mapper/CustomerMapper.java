package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.CustomerRequest;
import ru.fafurin.publishing.model.Customer;

public class CustomerMapper {

    public static Customer getCustomer(Customer customer, CustomerRequest customerRequest) {
        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        return customer;
    }

}
