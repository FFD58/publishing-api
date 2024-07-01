package ru.fafurin.publishing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.exception.CustomerNotFoundException;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.service.CustomerService;
import ru.fafurin.publishing.service.CustomerServiceContract;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@Tag(
        name = "Заказчики",
        description = "Методы для работы с заказчиками"
)
public class CustomerController {

    @Autowired
    private CustomerServiceContract customerService;

    @GetMapping
    @Operation(summary = "Получить информацию обо всех заказчиках")
    public ResponseEntity<List<Customer>> list() {
        List<Customer> customers = customerService.getAll();
        if (customers.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о заказчике по идентификатору")
    public ResponseEntity<Customer> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(customerService.get(id));
        } catch (CustomerNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Сохранить нового заказчика")
    public ResponseEntity<Customer> save(
            @RequestBody @Valid CustomerRequest customerRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.save(customerRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Изменить заказчика по идентификатору")
    public ResponseEntity<Customer> updateCustomer(
            @RequestBody @Valid CustomerRequest customerRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(customerService.update(id, customerRequest));
        } catch (CustomerNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказчика по идентификатору")
    public ResponseEntity<String> deleteCustomer(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            customerService.delete(id);
            return ResponseEntity.ok(String.format("Customer with id: %d deleted", id));
        } catch (CustomerNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
