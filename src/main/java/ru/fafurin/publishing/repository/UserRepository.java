package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
