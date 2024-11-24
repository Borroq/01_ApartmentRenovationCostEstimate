package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.Security.GrantedAuthorityImpl;
import com.example.ApartmentRenovationCostEstimate.cart.Cart;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // -> Ustawia domyślnie poziom dostępu na private dla wszystkich pól w klasie
@Table(name = "users")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Name cannot be null")
    @Size(min = 3, max = 70)
    String name;

    @NotBlank(message = "Surname cannot be null")
    @Size(min = 3, max = 70)
    String surname;

    @Size(max = 70)
    String nick;

    @Size(min = 6, max = 200)
    String password;

    @NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    String email;


    public User(String name, String surname, String nick, String password, String email) {
        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.password = password;
        this.email = email;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Cart> carts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<GrantedAuthorityImpl> grantedAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
