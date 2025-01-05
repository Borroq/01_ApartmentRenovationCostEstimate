package com.example.ApartmentRenovationCostEstimate.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // Zapewnia, że Twoja baza danych testowa nie jest automatycznie zastępowana
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnUserByGivenEmail() {
        // Given
        String email = "janko_new.kowalski@gmail.com";

        //When
        Optional<User> optionalUser = userRepository.findByEmail(email);

        //Then
        assertThat(optionalUser).isPresent().hasValueSatisfying(
                user -> {
                    assertThat(user.getEmail()).isEqualTo(email);
                    assertThat(user.getId()).isEqualTo(17L);
                    assertThat(user.getName()).isEqualTo("Janko_new");
                    assertThat(user.getNick()).isEqualTo("Tester_new");
                }
        );

    }


    @Test
    void shouldReturnFindUserByNonExistingEmail() {
        // Given
        String email = "NonExistEmail@gmail.com";

        //When
        Optional<User> optionalUser = userRepository.findByEmail(email);

        //Then
        assertThat(optionalUser).isNotPresent();
    }
}
