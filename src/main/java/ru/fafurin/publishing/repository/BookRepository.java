package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.entity.Book;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
