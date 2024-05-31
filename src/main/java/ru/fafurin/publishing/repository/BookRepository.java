package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
