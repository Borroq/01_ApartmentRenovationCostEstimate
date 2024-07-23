package com.example.ApartmentRenovationCostEstimate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String surname;
    private String nick;
    private String password;
    private String roles; // ROLES_USER,ROLES_ADMIN
    private String email;

    public User(Integer id, String name, String surname, String nick, String password, String roles, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }

}
