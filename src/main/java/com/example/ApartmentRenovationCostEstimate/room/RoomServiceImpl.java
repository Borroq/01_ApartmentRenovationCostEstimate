package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.exceptions.room.RoomNotFoundException;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomUpdateDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomResponseDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomSaveDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    public RoomServiceImpl(RoomRepository roomRepository, ModelMapper modelMapper) {
        this.roomRepository = roomRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public RoomResponseDto createRoom(RoomSaveDto roomSaveDto) {
        Room room = modelMapper.map(roomSaveDto, Room.class);
        Room savedRoom = roomRepository.save(room);

        return modelMapper.map(savedRoom, RoomResponseDto.class);
    }


    @Override
    public RoomResponseDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        return modelMapper.map(room, RoomResponseDto.class);
    }


    @Override
    public Page<RoomResponseDto> getAllRoom(Pageable pageable) {
        Page<Room> roomsPage = roomRepository.findAll(pageable);

        if (roomsPage.isEmpty()) {
            throw new RoomNotFoundException("Room not found");
        }

        return roomsPage
                .map(room -> modelMapper.map(room, RoomResponseDto.class));
    }


    @Override
    @Transactional
    public RoomResponseDto updateRoom(RoomUpdateDto roomUpdateDto) {
        Room existingRoom = roomRepository.findById(roomUpdateDto.getId())
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        existingRoom.setName(roomUpdateDto.getName());
        existingRoom.setFloorArea(roomUpdateDto.getFloorArea());
        existingRoom.setWallArea(roomUpdateDto.getWallArea());

        return modelMapper.map(existingRoom, RoomResponseDto.class);
    }


    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        roomRepository.deleteById(roomId);
    }
}
