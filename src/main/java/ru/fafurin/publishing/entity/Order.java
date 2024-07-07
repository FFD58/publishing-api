package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer number;
    LocalDateTime deadline;
    String comment;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;
    LocalDateTime finishedAt;
    boolean isDeleted = false;
    boolean isReported = false;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToOne
    @JoinColumn(name = "book_id")
    Book book;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    List<UserTask> tasks;

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
