package ru.fafurin.publishing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.model.BookType;
import ru.fafurin.publishing.dto.BookTypeRequest;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.mapper.BookTypeMapper;
import ru.fafurin.publishing.repository.BookTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookTypeService {

    @Autowired
    private BookTypeRepository repository;

    public List<BookType> getAll() {
        List<BookType> bookTypes = repository.findAll();
        return bookTypes.stream()
                .filter(b -> b.getIsDeleted().equals(false))
                .collect(Collectors.toList());
    }

    public BookType get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookTypeNotFoundException(id));
    }

    public BookType save(BookTypeRequest bookTypeResponse) {
        return repository.save(
                BookTypeMapper.getBookType(new BookType(), bookTypeResponse));
    }

    public BookType update(Long id, BookTypeRequest bookTypeResponse) {
        BookType bookType = repository.findById(id)
                .orElseThrow(() -> new BookTypeNotFoundException(id));

        return repository.save(
                BookTypeMapper.getBookType(bookType, bookTypeResponse));
    }

    /**
     * Безопасно удалить тип книги по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     * @param id - идентификатор типа книги
     */
    public void delete(Long id) {
        BookType bookType = repository.findById(id)
                .orElseThrow(() -> new BookTypeNotFoundException(id));
        bookType.setIsDeleted(true);
        repository.save(bookType);
    }

}
