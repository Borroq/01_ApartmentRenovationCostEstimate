package com.example.ApartmentRenovationCostEstimate.entityDomain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String surname;
    private String nick;
    private String email;

    public User() {
    }

    public User(String name, String surname, String nick, String email) {
        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.email = email;
    }
}
