package com.example.ApartmentRenovationCostEstimate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*W Cart, relacja @ManyToOne oznacza, że wiele koszyków może być
    przypisanych do jednego użytkownika. Dlatego Cart ma pole User user.*/
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;


    private String name; //Nazwa koszyka

}
