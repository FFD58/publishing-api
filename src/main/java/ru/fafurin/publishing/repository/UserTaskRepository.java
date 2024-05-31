package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.UserTask;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
}
