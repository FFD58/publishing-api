package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.entity.BookType;

import java.util.Optional;
@Repository
public interface BookTypeRepository extends JpaRepository<BookType, Long> {
    Optional<BookType> findByTitle(String title);
}
