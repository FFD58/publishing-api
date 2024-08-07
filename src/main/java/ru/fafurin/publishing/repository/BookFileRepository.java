package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.entity.BookFile;
@Repository
public interface BookFileRepository extends JpaRepository<BookFile, Long> {
}
