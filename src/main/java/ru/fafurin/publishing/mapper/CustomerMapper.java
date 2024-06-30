package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.dto.response.CustomerResponse;
import ru.fafurin.publishing.entity.Customer;

public class CustomerMapper {

    public static Customer getCustomer(Customer customer, CustomerRequest customerRequest) {
        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        return customer;
    }

    public static CustomerResponse getCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

}
