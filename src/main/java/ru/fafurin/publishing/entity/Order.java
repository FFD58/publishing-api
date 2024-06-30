package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private String deadline;
    private String comment;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime finishedAt;
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserTask> tasks;

    public void setBook(Book book) {
        this.book = book;
        book.setOrder(this);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.addOrder(this);
    }

    public void addTask(UserTask userTask) {
        if (userTask != null) {
            this.tasks.add(userTask);
        }
    }
}
