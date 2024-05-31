package ru.fafurin.publishing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "types")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;

    private Boolean isDeleted = false;
}
