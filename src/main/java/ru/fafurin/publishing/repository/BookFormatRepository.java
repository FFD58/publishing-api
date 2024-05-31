package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.BookFormat;

import java.util.Optional;

public interface BookFormatRepository extends JpaRepository<BookFormat, Long> {
    Optional<BookFormat> findByTitle(String title);
}
