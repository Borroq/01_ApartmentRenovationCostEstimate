package com.example.ApartmentRenovationCostEstimate.Security;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
/*TODO Wyjaśnienie: @EnableMethodSecurity(prePostEnabled = true)
   służą do zarządzania autoryzacją na poziomie metod.
   Dla metod, które mają być chronione należy dodać adnotację, np.: @PreAuthorize("hasRole('ADMIN')") lub
        @PreAuthorize("hasRole('ADMIN') and #userId == authentication.principal.id") ->  sprawdza uprawnienia przed wykonaniem metody
        public void someMethod(Long userId) {
         // dostęp mają użytkownicy z rolą ADMIN oraz gdy userId zgadza się z ID użytkownika
        }
   lub
        @PostAuthorize("returnObject.username == authentication.name") -> sprawdza uprawnienia po wykonaniu metody i pozwala na autoryzację na podstawie wyniku metody.
        public User getUserDetails(String username) {
            // Metoda zwraca obiekt User
        }
 */
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/static/css/**", "/static/js/**", "/templates/**").permitAll()
                        .requestMatchers("/login/**", "/logout/**").permitAll() //Czy to aby na pewno jest potrzebne? Patrz niżej
                        .requestMatchers("/access-denied").permitAll()

                        .requestMatchers(HttpMethod.POST, "/users").anonymous()
                        .requestMatchers(HttpMethod.PUT, "/users").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "api/users/{id}").hasRole("ADMIN")

                        .requestMatchers("/**", "/users/**").permitAll() // DISABLING SECURITY

                        .requestMatchers("/home/**", "/rooms/**", "/carts/**", "/products/**", "/users/**", "/api/database/**", "/api/users/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new AppAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Ustawienie przekierowania po pomyślnym zalogowaniu
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .rememberMe(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        //TODO Algorytm BCryptPasswordEncoder("siła") daje mozliwość ustawienia siły algorytmu.
        // Wg. dokumentacji należy tak dobrać parametr siły, tak aby weryfikacja hasła zajmowała około 1s.
        return new BCryptPasswordEncoder();
    }

    /*todo Sprawdzić dokładnie jak działa AuthenticationProvider
       AuthenticationProvider to interfejs w Spring Security, który definiuje sposób weryfikacji danych logowania użytkownika.
       Jest częścią mechanizmu uwierzytelniania i jego zadaniem jest sprawdzenie, czy dostarczone dane użytkownika (np. nazwa użytkownika i hasło) są poprawne.
*/

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // używa UserDetailsService do wczytania danych użytkownika
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // używa PasswordEncoder do weryfikacji hasła
        return authenticationProvider;
    }

/*    todo Sprawdzić dokładnie jak działa AuthenticationManager
       AuthenticationManagerto kluczowy komponent w Spring Security, który obsługuje uwierzytelnianie użytkowników.
       Jest to interfejs, który próbuje uwierzytelnić przekazany obiekt **`Authentication`** (np. nazwę użytkownika i hasło).*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
