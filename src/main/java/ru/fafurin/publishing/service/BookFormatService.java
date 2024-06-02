package ru.fafurin.publishing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.BookFormatRequest;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.mapper.BookFormatMapper;
import ru.fafurin.publishing.model.BookFormat;
import ru.fafurin.publishing.repository.BookFormatRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookFormatService {

    @Autowired
    private BookFormatRepository repository;

    /**
     * Получить список всех форматов книг
     * @return список всех форматов книг
     */
    public List<BookFormat> getAll() {
        List<BookFormat> booFormats = repository.findAll();
        return booFormats.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить формат книги по идентификатору
     * @param id - идентификатор формата книги
     * @return формат книги или выбрасывается исключение,
     *         если формат книги не найден по идентификатору
     */
    public BookFormat get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookFormatNotFoundException(id));
    }

    /**
     * Сохранить новый формат книги
     * @param bookFormatRequest - данные для сохранения нового формата книги
     * @return сохраненный формат книги
     */
    public BookFormat save(BookFormatRequest bookFormatRequest) {
        return repository.save(
                BookFormatMapper.getBookFormat(new BookFormat(), bookFormatRequest));
    }

    /**
     * Изменить данные существующего формата книги
     * @param id - идентификатор формата книги
     * @param bookFormatRequest - данные для изменения существующего формата книги
     * @return - измененный формат книги или выбрасывается исключение,
     *           если формат книги не найден по идентификатору
     */
    public BookFormat update(Long id, BookFormatRequest bookFormatRequest) {
        BookFormat bookFormat = repository.findById(id)
                .orElseThrow(() -> new BookFormatNotFoundException(id));

        return repository.save(
                BookFormatMapper.getBookFormat(bookFormat, bookFormatRequest));
    }

    /**
     * Безопасно удалить формат книги по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     * @param id - идентификатор формата книги
     */
    public void delete(Long id) {
        BookFormat bookFormat = repository.findById(id)
                .orElseThrow(() -> new BookFormatNotFoundException(id));
        bookFormat.setDeleted(true);
        repository.save(bookFormat);
    }

}
