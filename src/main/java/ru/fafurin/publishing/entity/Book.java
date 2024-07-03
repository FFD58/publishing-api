package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BookFile> files;

    private List<String> authors;

    @ManyToOne
    @JoinColumn(name = "book_format_id")
    private BookFormat format;

    @ManyToOne
    @JoinColumn(name = "book_type_id")
    private BookType type;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private boolean isDeleted = false;

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

    @Override
    public String toString() {
        return  "title='" + title + '\'' +
                ", authors=" + authors +
                ", format=" + format.getTitle() +
                ", type=" + type.getTitle();
    }
}
