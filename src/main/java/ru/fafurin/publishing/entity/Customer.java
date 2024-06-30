package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customers")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
    private boolean isDeleted = false;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;

    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
        }
    }
}
