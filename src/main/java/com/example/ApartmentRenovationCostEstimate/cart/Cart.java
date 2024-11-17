package com.example.ApartmentRenovationCostEstimate.cart;

import com.example.ApartmentRenovationCostEstimate.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /*W Cart, relacja @ManyToOne oznacza, że wiele koszyków może być
    przypisanych do jednego użytkownika. Dlatego Cart ma pole User user.*/
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<CartItem> cartItems;


    String name;
    BigDecimal totalCost;

}
