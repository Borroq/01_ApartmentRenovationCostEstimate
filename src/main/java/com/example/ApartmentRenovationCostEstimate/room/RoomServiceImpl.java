package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.exceptions.room.RoomNotFoundException;
import com.example.ApartmentRenovationCostEstimate.room.DTOs.RoomDto;
import com.example.ApartmentRenovationCostEstimate.room.DTOs.RoomSaveDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, ModelMapper modelMapper) {
        this.roomRepository = roomRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Room createRoom(RoomSaveDto roomSaveDto) {
        Room room = modelMapper.map(roomSaveDto, Room.class);

        return roomRepository.save(room);
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoom() {
        Iterable<Room> rooms = roomRepository.findAll();

        return StreamSupport.stream(rooms.spliterator(), false)
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Room updateRoom(RoomDto roomDto) {
        Room existingRoom = roomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        modelMapper.map(roomDto, existingRoom);

        return roomRepository.save(existingRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        roomRepository.deleteById(roomId);
    }
}
