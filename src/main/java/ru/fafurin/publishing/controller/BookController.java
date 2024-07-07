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
import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.entity.Book;
import ru.fafurin.publishing.exception.BookNotFoundException;
import ru.fafurin.publishing.service.BookService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@Tag(
        name = "Книги",
        description = "Методы для работы с книгами"
)
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Получить информацию обо всех книгах")
    public ResponseEntity<List<Book>> list() {
        List<Book> books = bookService.getAll();
        if (books.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о книге по идентификатору")
    public ResponseEntity<Book> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookService.get(id));
        } catch (BookNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @Operation(summary = "Сохранить новую книгу")
    public ResponseEntity<Book> save(
            @RequestBody @Valid BookRequest bookRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.save(bookRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить данные о книге по идентификатору")
    public ResponseEntity<Book> update(
            @RequestBody @Valid BookRequest bookRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bookService.update(id, bookRequest));
        } catch (BookNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить книгу по идентификатору")
    public ResponseEntity<String> deleteBook(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.ok(String.format("Book with id: %d deleted", id));
        } catch (BookNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
