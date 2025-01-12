package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.exceptions.room.RoomNotFoundException;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomResponseDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomSaveDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomUpdateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomServiceUnitTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ModelMapper modelMapper;

    private RoomServiceImpl underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new RoomServiceImpl(roomRepository, modelMapper);
    }

    @AfterEach
    void afterAll() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("It should create Room Successfully")
    void itShouldCreateNewRoomSuccessfully() {
        // Given
        String name = "Room";
        Double floorArea = 2.2;
        Double wallArea = 4.8;

        RoomSaveDto roomSaveDto = new RoomSaveDto(
                name,
                floorArea,
                wallArea
        );

        Room room = new Room();
        room.setName(name);
        room.setFloorArea(floorArea);
        room.setWallArea(wallArea);

        RoomResponseDto expectedResponse = new RoomResponseDto();
        expectedResponse.setName(name);
        expectedResponse.setFloorArea(floorArea);
        expectedResponse.setWallArea(wallArea);

        //Mocking
        when(modelMapper.map(roomSaveDto, Room.class)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);
        when(modelMapper.map(room, RoomResponseDto.class)).thenReturn(expectedResponse);

        //When
        RoomResponseDto actualResponse = underTest.createRoom(roomSaveDto);

        //Then
        assertNotNull(actualResponse);
        assertAll(
                () -> verify(roomRepository, times(1)).save(room),
                () -> verify(modelMapper, times(1)).map(roomSaveDto, Room.class),
                () -> verify(modelMapper, times(1)).map(room, RoomResponseDto.class),

                () -> assertEquals(name, actualResponse.getName()),
                () -> assertEquals(floorArea, actualResponse.getFloorArea()),
                () -> assertEquals(wallArea, actualResponse.getWallArea())
        );
    }

    @Test
    @DisplayName("It should find room by given id successfully")
    void itShouldReturnRoomByGivenIdSuccessfully() {
        // Given
        Long roomId = 1L;
        String name = "Room";
        Double floorArea = 2.2;
        Double wallArea = 4.8;

        Room room = new Room();
        room.setId(roomId);
        room.setName(name);
        room.setFloorArea(floorArea);
        room.setWallArea(wallArea);

        RoomResponseDto expectedResponse = new RoomResponseDto();
        expectedResponse.setName(name);
        expectedResponse.setFloorArea(floorArea);
        expectedResponse.setWallArea(wallArea);

        //Mocking
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(modelMapper.map(room, RoomResponseDto.class)).thenReturn(expectedResponse);

        //When
        RoomResponseDto actualResponse = underTest.getRoomById(roomId);

        //Then
        assertNotNull(actualResponse);
        assertAll(
                () -> verify(roomRepository, times(1)).findById(roomId),
                () -> verify(modelMapper, times(1)).map(room, RoomResponseDto.class),

                () -> assertEquals(name, actualResponse.getName()),
                () -> assertEquals(floorArea, actualResponse.getFloorArea()),
                () -> assertEquals(wallArea, actualResponse.getWallArea())
        );
    }

    @Test
    @DisplayName("It should throw RoomNotFoundException when room doesn't exist")
    void itShouldThrowRoomNotFoundExceptionWhenRoomNotExist() {
        //Given
        Long roomId = 1L;

        //Mocking
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(RoomNotFoundException.class, () -> underTest.getRoomById(roomId));

        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    @DisplayName("It should return room list when rooms exist")
    void itReturnRoomListWhenRoomExist() {
        // Given
        Long roomId = 1L;
        String name = "Room";
        Double floorArea = 2.2;
        Double wallArea = 4.8;

        Room room = new Room();
        room.setId(roomId);
        room.setName(name);
        room.setFloorArea(floorArea);
        room.setWallArea(wallArea);

        RoomResponseDto expectedResponse = new RoomResponseDto();
        expectedResponse.setName(name);
        expectedResponse.setFloorArea(floorArea);
        expectedResponse.setWallArea(wallArea);

        //Mocking
        Pageable pageable = PageRequest.of(0, 10);
        Page<Room> roomsPage = new PageImpl<>(Collections.singletonList(room), pageable, 1);

        when(roomRepository.findAll(pageable)).thenReturn(roomsPage);
        when(modelMapper.map(room, RoomResponseDto.class)).thenReturn(expectedResponse);

        //When
        Page<RoomResponseDto> actualResponse = underTest.getAllRooms(pageable);

        //Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getName(), actualResponse.getContent().get(0).getName());
        assertEquals(expectedResponse.getFloorArea(), actualResponse.getContent().get(0).getFloorArea());
        assertEquals(expectedResponse.getWallArea(), actualResponse.getContent().get(0).getWallArea());

        verify(roomRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(1)).map(room, RoomResponseDto.class);
    }

    @Test
    @DisplayName("It should return RoomNotFoundException when rooms doesn't exist")
    void itShouldReturnRoomListWhenRoomExistWhenNoRoomsExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Room> roomsPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(roomRepository.findAll(pageable)).thenReturn(roomsPage);

        //When & Then
        RoomNotFoundException throwException = assertThrows(RoomNotFoundException.class, () -> underTest.getAllRooms(pageable));

        assertEquals("Room not found!", throwException.getMessage());

        verify(roomRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("It should update Room")
    void itShouldUpdateRoom() {
        //Given
        Long roomId = 1L;
        String updatedName = "updated Room";
        Double updateFloorArea = 2.9;
        Double updatedWallArea = 4.0;

        RoomUpdateDto roomUpdateDto = new RoomUpdateDto();
        roomUpdateDto.setId(roomId);
        roomUpdateDto.setName(updatedName);
        roomUpdateDto.setFloorArea(updateFloorArea);
        roomUpdateDto.setWallArea(updatedWallArea);

        Room existingRoom = new Room();
        existingRoom.setId(roomId);
        existingRoom.setName("Old Room");
        existingRoom.setFloorArea(2.0);
        existingRoom.setWallArea(2.5);

        Room updatedRoom = new Room();
        updatedRoom.setId(roomId);
        updatedRoom.setName(updatedName);
        updatedRoom.setFloorArea(updateFloorArea);
        updatedRoom.setWallArea(updatedWallArea);

        RoomResponseDto roomResponseDto = new RoomResponseDto();
        roomResponseDto.setName(updatedName);
        roomResponseDto.setFloorArea(updateFloorArea);
        roomResponseDto.setWallArea(updatedWallArea);

        //Mocking
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(existingRoom));
        when(modelMapper.map(existingRoom, RoomResponseDto.class)).thenReturn(roomResponseDto);

        //When
        RoomResponseDto actualResponse = underTest.updateRoom(roomUpdateDto);

        //Then

        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(updatedName, actualResponse.getName()),
                () -> assertEquals(updateFloorArea, actualResponse.getFloorArea()),
                () -> assertEquals(updatedWallArea, actualResponse.getWallArea()),

                () -> verify(roomRepository, times(1)).findById(roomId),
                () -> verify(modelMapper, times(1)).map(existingRoom, RoomResponseDto.class)
        );


    }

    @Test
    @DisplayName("It should throw RoomNotFoundException when room doesn't exist")
    void itShouldThrowExceptionWhenRoomNotFound() {
        //Given
        Long roomId = 1L;
        RoomUpdateDto roomUpdateDto = new RoomUpdateDto();
        roomUpdateDto.setId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> underTest.updateRoom(roomUpdateDto));

        verify(roomRepository, times(0)).save(any());
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    @DisplayName("It should delete room")
    void itShouldDeleteRoom() {
        // Given
        Long roomId = 1L;
        String name = "Room";
        Double floorArea = 2.2;
        Double wallArea = 4.8;

        Room room = new Room();
        room.setName(name);
        room.setFloorArea(floorArea);
        room.setWallArea(wallArea);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        //When
        underTest.deleteRoom(roomId);

        //Then
        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).deleteById(roomId);
    }

    @Test
    @DisplayName("It should throw RoomNotFoundException when try delete room and room doesn't exist")
    void itShouldRoomNotFoundExceptionWhenTryDeleteRoom() {
        Long roomId = 1L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> underTest.deleteRoom(roomId));

        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(0)).deleteById(roomId);
    }
}
