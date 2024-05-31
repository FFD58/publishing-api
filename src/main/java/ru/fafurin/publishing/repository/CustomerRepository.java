package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}

