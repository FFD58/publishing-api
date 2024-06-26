package ru.fafurin.publishing.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.OrderAllInfoResponse;
import ru.fafurin.publishing.dto.response.OrderResponse;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.service.FileGateway;
import ru.fafurin.publishing.service.OrderServiceContract;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
@Tag(
        name = "Заказы",
        description = "Методы для работы с заказами"
)
public class OrderController {

    private final OrderServiceContract orderService;
    private final FileGateway fileGateway;

    private final Counter addOrderCounter = Metrics.counter("add_order_count");

    @GetMapping
    @Operation(summary = "Получить информацию обо всех заказах со статусом AWAIT")
    public ResponseEntity<List<OrderResponse>> listAwaitingOrders() {
        List<OrderResponse> orders = orderService.getAwaitingOrders();

        System.out.println(orders);

        if (orders.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить полную информацию о заказе по идентификатору")
    public ResponseEntity<OrderAllInfoResponse> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(orderService.get(id));
        } catch (OrderNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Сохранить новый заказ")
    public ResponseEntity<Order> save(
            @RequestBody @Valid OrderRequest orderRequest) {

        System.out.println(orderRequest);

        Order order = orderService.save(orderRequest);

        addOrderCounter.increment();

        String filename = order.getId() + "_"
                + order.getBook().getTitle() + "_"
                + LocalDateTime.now().getNano() + ".txt";

        filename = filename.replace(" ", "_");
        fileGateway.writeToFile(filename, String.valueOf(order));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Изменить данные о заказе по идентификатору")
    public ResponseEntity<Order> updateOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(orderService.update(id, orderRequest));
        } catch (OrderNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ по идентификатору")
    public ResponseEntity<String> deleteOrder(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            orderService.delete(id);
            return ResponseEntity.ok(String.format("Order with id: %d deleted", id));
        } catch (OrderNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
