package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.entity.Status;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Status status);

    @Query(value = """
            SELECT * FROM orders o 
            WHERE o.is_reported = false
            AND o.deadline is not null 
            AND o.deadline between :start and :end
            """, nativeQuery = true)
    List<Order> findAllSoonOrders(@Param("start") Timestamp start, @Param("end") Timestamp end);
}
