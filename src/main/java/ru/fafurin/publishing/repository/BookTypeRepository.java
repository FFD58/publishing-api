package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.BookType;

import java.util.Optional;

public interface BookTypeRepository extends JpaRepository<BookType, Long> {
    Optional<BookType> findByTitle(String title);
}
