package ru.fafurin.publishing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.order.OrderAddInfoResponse;
import ru.fafurin.publishing.dto.response.order.OrderAllInfoResponse;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.service.OrderService;

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

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Получить информацию обо всех заказах со статусом AWAIT")
    public ResponseEntity<List<OrderAddInfoResponse>> listAwaitingOrders() {
        List<OrderAddInfoResponse> orders = orderService.getAwaitingOrders();

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

    @PostMapping
    @Operation(summary = "Сохранить новый заказ")
    public ResponseEntity<Order> save(
            @RequestBody @Valid OrderRequest orderRequest) {
        Order order = orderService.save(orderRequest);
        log.info("Заказ с идентификатором: {} сохранен", order.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить данные о заказе по идентификатору")
    public ResponseEntity<Order> updateOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            Order updatedOrder = orderService.update(id, orderRequest);
            log.info("Заказ с идентификатором: {} изменен", id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updatedOrder);
        } catch (OrderNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ по идентификатору")
    public ResponseEntity<String> deleteOrder(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            orderService.delete(id);
            log.info("Заказ с идентификатором: {} удален", id);
            return ResponseEntity.ok(String.format("Заказ с идентификатором: %d удален", id));
        } catch (OrderNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{orderId}/upload-file")
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "Идентификатор") @PathVariable Long orderId,
            @RequestParam("file") MultipartFile file) {
        try {
            String filename = orderService.uploadFile(orderId, file);
            log.info("{} успешно загружен", filename);
            return ResponseEntity.ok("Файл успешно загружен: " + filename);
        } catch (OrderNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
