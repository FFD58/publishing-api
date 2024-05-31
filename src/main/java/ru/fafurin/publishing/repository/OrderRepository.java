package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.Order;
import ru.fafurin.publishing.model.Status;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Status status);
}