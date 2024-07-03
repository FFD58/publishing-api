package ru.fafurin.publishing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.exception.UserNotFoundException;
import ru.fafurin.publishing.entity.User;
import ru.fafurin.publishing.repository.UserRepository;
import ru.fafurin.publishing.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    /**
     * Получить список всех сотрудников
     *
     * @return список всех сотрудников
     */
    public List<User> getAll() {
        List<User> users = repository.findAll();
        return users.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить сотрудника по идентификатору
     *
     * @param id - идентификатор сотрудника
     * @return сотрудник или выбрасывается исключение,
     * если сотрудник не найден по идентификатору
     */
    @Override
    public User get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Сохранить сотрудника
     *
     * @param user - данные сотрудника
     * @return сохраненный сотрудник
     */
    @Override
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Получить сотрудника по имени
     *
     * @param username - имя сотрудника
     * @return сотрудник или выбрасывается исключение, если сотрудника с таким именем нет в базе
     */
    @Override
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Безопасно удалить сотрудника по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     *
     * @param id - идентификатор сотрудника
     */
    @Override
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setDeleted(true);
        repository.save(user);
    }

    /**
     * Получить объект UserDetailsService для аутентификации
     * @return объект UserDetailsService
     */
    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
