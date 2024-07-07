package ru.fafurin.publishing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fafurin.publishing.dto.request.BookFormatRequest;
import ru.fafurin.publishing.entity.BookFormat;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.service.BookFormatService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books/formats")
@Tag(
        name = "Форматы книг",
        description = "Методы для работы с книжными форматами"
)
public class BookFormatController {

    private final BookFormatService bookFormatService;

    @GetMapping
    @Operation(summary = "Получить информацию обо всех книжных форматах")
    public ResponseEntity<List<BookFormat>> list() {
        List<BookFormat> bookFormats = bookFormatService.getAll();
        if (bookFormats.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookFormats);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о формате книги по идентификатору")
    public ResponseEntity<BookFormat> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookFormatService.get(id));
        } catch (BookFormatNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @Operation(summary = "Сохранить новый книжный формат")
    public ResponseEntity<BookFormat> save(
            @RequestBody @Valid BookFormatRequest bookFormatRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookFormatService.save(bookFormatRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить книжный формат по идентификатору")
    public ResponseEntity<BookFormat> updateBookFormat(
            @RequestBody @Valid BookFormatRequest bookFormatRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookFormatService.update(id, bookFormatRequest));
        } catch (BookFormatNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить книжный формат по идентификатору")
    public ResponseEntity<String> deleteBookFormat(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            bookFormatService.delete(id);
            return ResponseEntity.ok(String.format("Book format with id: %d deleted", id));
        } catch (BookFormatNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
