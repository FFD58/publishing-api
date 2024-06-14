package ru.fafurin.publishing.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
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
import ru.fafurin.publishing.dto.BookTypeRequest;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.model.BookType;
import ru.fafurin.publishing.service.BookTypeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/books/types")
@Tag(
        name = "Типы книг",
        description = "Методы для работы с типами книг"
)
public class BookTypeController {

    @Autowired
    private BookTypeService bookTypeService;

    private final Counter addTypeCounter = Metrics.counter("add_type_count");

    @GetMapping
    @Operation(summary = "Получить информацию обо всех типах книг")
    public ResponseEntity<List<BookType>> list() {
        List<BookType> bookTypes = bookTypeService.getAll();
        if (bookTypes.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о типе книги по идентификатору")
    public ResponseEntity<BookType> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookTypeService.get(id));
        } catch (BookTypeNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Сохранить новый тип книги")
    public ResponseEntity<BookType> save(
            @RequestBody @Valid BookTypeRequest bookTypeRequest) {
        addTypeCounter.increment();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookTypeService.save(bookTypeRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Изменить тип книги по идентификатору")
    public ResponseEntity<BookType> updateBookType(
            @RequestBody @Valid BookTypeRequest bookTypeRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookTypeService.update(id, bookTypeRequest));
        } catch (BookTypeNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тип книги по идентификатору")
    public ResponseEntity<String> deleteBookType(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            bookTypeService.delete(id);
            return ResponseEntity.ok(String.format("Book type with id: %d deleted", id));
        } catch (BookTypeNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
