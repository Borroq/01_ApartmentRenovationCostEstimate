package com.example.ApartmentRenovationCostEstimate.user;


import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserAlreadyExistsException;
import com.example.ApartmentRenovationCostEstimate.exceptions.user.UserNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        //Mocking
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //When & Then
        assertThrows(UserAlreadyExistsException.class, () -> underTest.createUser(userSaveDto));

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(0)).save(any());
    }


    @Test
    void itShouldReturnUserResponseDtoWhenUserExists() {
        // Given
        Long userId = 1L;
        String name = "Janko";
        String surname = "Kowalski";
        String nick = "Tester";
        String password = "testPassword";
        String email = "janko.kowalski@gmail.com";
        String role = "ROLE_USER";

        User user = new User();
        user.setId(userId);
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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(expectedResponse);

        //When
        UserResponseDto actualResponse = underTest.getUserById(userId);

        //Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getSurname(), actualResponse.getSurname());
        assertEquals(expectedResponse.getNick(), actualResponse.getNick());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());

        verify(userRepository, times(1)).findById(userId);
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }


    @Test
    void itShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        //Given
        Long userId = 1L;

        //Mocking
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> underTest.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void itShouldReturnUserListWhenUsersExist() {
        //Given
        Long id = 1L;
        String name = "Janko";
        String surname = "Kowalski";
        String nick = "Tester";
        String email = "janko.kowalski@gmail.com";

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setNick(nick);
        user.setEmail(email);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(name);
        userResponseDto.setSurname(surname);
        userResponseDto.setNick(nick);
        userResponseDto.setEmail(email);

        //Mocking
        Pageable pageable = PageRequest.of(0,10);
        Page<User> usersPage = new PageImpl<>(Collections.singletonList(user), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(usersPage);
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        //When
        Page<UserResponseDto> actualResponse = underTest.getAllUsers(pageable);

        //Then
        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getTotalElements());
        assertEquals(userResponseDto.getName(), actualResponse.getContent().get(0).getName());
        assertEquals(userResponseDto.getSurname(), actualResponse.getContent().get(0).getSurname());
        assertEquals(userResponseDto.getNick(), actualResponse.getContent().get(0).getNick());
        assertEquals(userResponseDto.getEmail(), actualResponse.getContent().get(0).getEmail());

        verify(userRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }


    @Test
    void itShouldThrowUserNotFoundExceptionWhenNoUsersExist() {
        //Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> usersPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(userRepository.findAll(pageable)).thenReturn(usersPage);

        //When &Then
        assertThrows(UserNotFoundException.class, () -> underTest.getAllUsers(pageable));

        verify(userRepository, times(1)).findAll(pageable);
    }


}
