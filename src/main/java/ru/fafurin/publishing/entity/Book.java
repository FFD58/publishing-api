package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @OneToMany(cascade = CascadeType.ALL)
    List<BookFile> files;

    List<String> authors;

    @ManyToOne
    @JoinColumn(name = "book_format_id")
    BookFormat format;

    @ManyToOne
    @JoinColumn(name = "book_type_id")
    BookType type;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    Order order;

    boolean isDeleted = false;

    public void setType(BookType type) {
        this.type = type;
        type.addBook(this);
    }

    public void setFormat(BookFormat format) {
        this.format = format;
        format.addBook(this);
    }

    public void addFiles(List<BookFile> files) {
        this.files.addAll(files);
        for (BookFile file : files) {
            file.setBook(this);
        }
    }
}
