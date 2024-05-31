package ru.fafurin.publishing.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Order order;
}
