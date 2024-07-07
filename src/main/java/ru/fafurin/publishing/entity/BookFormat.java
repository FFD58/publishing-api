package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "formats")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String title;

    String designation;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<Book> books;

    boolean isDeleted = false;

    public void addBook(Book book) {
        if (book != null) {
            this.books.add(book);
        }
    }
}
