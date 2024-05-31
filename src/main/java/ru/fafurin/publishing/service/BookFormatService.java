package ru.fafurin.publishing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.BookFormatRequest;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.mapper.BookFormatMapper;
import ru.fafurin.publishing.model.BookFormat;
import ru.fafurin.publishing.repository.BookFormatRepository;

import java.util.List;

@Service
public class BookFormatService {

    @Autowired
    private BookFormatRepository repository;

    public List<BookFormat> getAll() {
        return repository.findAll();
    }

    public BookFormat get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookFormatNotFoundException(id));
    }

    public BookFormat save(BookFormatRequest bookFormatRequest) {
        return repository.save(
                BookFormatMapper.getBookFormat(new BookFormat(), bookFormatRequest));
    }

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
        bookFormat.setIsDeleted(true);
        repository.save(bookFormat);
    }

}
