package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.entity.Customer;

public interface CustomerServiceContract extends CrudServiceContract<Customer, CustomerRequest> {
}
