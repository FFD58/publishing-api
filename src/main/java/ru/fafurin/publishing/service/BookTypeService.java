package ru.fafurin.publishing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.model.BookType;
import ru.fafurin.publishing.dto.request.BookTypeRequest;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.mapper.BookTypeMapper;
import ru.fafurin.publishing.repository.BookTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookTypeService {

    @Autowired
    private BookTypeRepository repository;

    /**
     * Получить список всех типов книг
     * @return список всех типов книг
     */
    public List<BookType> getAll() {
        List<BookType> bookTypes = repository.findAll();
        return bookTypes.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить тип книги по идентификатору
     * @param id - идентификатор типа книги
     * @return тип книги или выбрасывается исключение,
     *         если тип книги не найден по идентификатору
     */
    public BookType get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookTypeNotFoundException(id));
    }

    /**
     * Сохранить новый тип книги
     * @param bookTypeRequest - данные для сохранения нового типа книги
     * @return сохраненный тип книги
     */
    public BookType save(BookTypeRequest bookTypeRequest) {
        return repository.save(
                BookTypeMapper.getBookType(new BookType(), bookTypeRequest));
    }

    /**
     * Изменить данные существующего типа книги
     * @param id - идентификатор типа книги
     * @param bookTypeRequest - данные для изменения существующего типа книги
     * @return - измененный тип книги или выбрасывается исключение,
     *           если тип книги не найден по идентификатору
     */
    public BookType update(Long id, BookTypeRequest bookTypeRequest) {
        BookType bookType = repository.findById(id)
                .orElseThrow(() -> new BookTypeNotFoundException(id));

        return repository.save(
                BookTypeMapper.getBookType(bookType, bookTypeRequest));
    }

    /**
     * Безопасно удалить тип книги по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     * @param id - идентификатор типа книги
     */
    public void delete(Long id) {
        BookType bookType = repository.findById(id)
                .orElseThrow(() -> new BookTypeNotFoundException(id));
        bookType.setDeleted(true);
        repository.save(bookType);
    }

}
