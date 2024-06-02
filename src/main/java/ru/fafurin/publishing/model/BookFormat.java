package ru.fafurin.publishing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "formats")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String designation;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;

    @AssertFalse
    private boolean isDeleted;
}
