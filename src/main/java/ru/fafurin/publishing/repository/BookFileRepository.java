package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.BookFile;

public interface BookFileRepository extends JpaRepository<BookFile, Long> {
}
