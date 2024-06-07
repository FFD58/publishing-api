package ru.fafurin.publishing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fafurin.publishing.model.Profile;
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
