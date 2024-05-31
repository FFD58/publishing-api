package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fafurin.publishing.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
