package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.entity.Status;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Status status);
}
