package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.entity.UserTask;
@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
}
