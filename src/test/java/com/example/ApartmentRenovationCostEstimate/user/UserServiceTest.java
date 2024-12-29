package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserAlreadyExistsException;
import com.example.ApartmentRenovationCostEstimate.security.GrantedAuthorityImpl;
import com.example.ApartmentRenovationCostEstimate.security.RoleRepository;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserResponseDto;
import com.example.ApartmentRenovationCostEstimate.user.dtos.UserSaveDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    private UserServiceImpl underTest;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserServiceImpl(userRepository, modelMapper, passwordEncoder, roleRepository);
    }

    @AfterEach
    void afterAll() throws Exception{
            autoCloseable.close();
    }


    @Test
    void itShouldCreateNewUser() {
        // Given
        String name = "Janko";
        String surname = "Kowalski";
        String nick = "Tester";
        String password = "testPassword";
        String email = "janko.kowalski@gmail.com";
        String role = "ROLE_USER";

        UserSaveDto userSaveDto = new UserSaveDto(
                name,
                surname,
                nick,
                password,
                email,
                role);

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setNick(nick);
        user.setPassword(password);
        user.setEmail(email);

        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl();
        grantedAuthority.setAuthority(role);
        user.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setName(name);
        expectedResponse.setSurname(surname);
        expectedResponse.setNick(nick);
        expectedResponse.setEmail(email);
        expectedResponse.setRole(role);


        //Mocking
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(modelMapper.map(userSaveDto, User.class)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(expectedResponse);

        //When
        UserResponseDto actualResponse = underTest.createUser(userSaveDto);

        //Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getSurname(), actualResponse.getSurname());
        assertEquals(expectedResponse.getNick(), actualResponse.getNick());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        assertEquals(expectedResponse.getRole(), actualResponse.getRole());

        verify(userRepository,times(1)).findByEmail(userSaveDto.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(userSaveDto.getPassword());
    }


    @Test
    void itShouldThrowExceptionIfUserExists () {
        // Given
        String name = "Janko";
        String surname = "Kowalski";
        String nick = "Tester";
        String password = "testPassword";
        String email = "janko.kowalski@gmail.com";
        String role = "ROLE_USER";

        UserSaveDto userSaveDto = new UserSaveDto(
                name,
                surname,
                nick,
                password,
                email,
                role);

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setNick(nick);
        user.setPassword(password);
        user.setEmail(email);

        //When
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //When & Then
        assertThrows(UserAlreadyExistsException.class, () -> underTest.createUser(userSaveDto));

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(0)).save(any());
    }

}
