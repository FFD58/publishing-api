package ru.fafurin.publishing.service;

import java.util.List;

public interface CrudService<E, R> {
    List<E> getAll();

    E get(Long id);

    E save(R request);

    E update(Long id, R request);

    void delete(Long id);
}
