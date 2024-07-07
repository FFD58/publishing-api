package ru.fafurin.publishing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(unique = true)
    String email;
    String phone;
    boolean isDeleted = false;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<Order> orders;

    public void addOrder(Order order) {
        if (order != null) {
            if (orders == null) {
                orders = new ArrayList<>();
            }
            orders.add(order);
        }
    }
}
