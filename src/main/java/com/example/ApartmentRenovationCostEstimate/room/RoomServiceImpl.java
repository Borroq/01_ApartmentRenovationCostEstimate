package com.example.ApartmentRenovationCostEstimate.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Override
    public List<Room> getAllRoom() {
        Iterable<Room> rooms = roomRepository.findAll();
        return StreamSupport.stream(rooms.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Room updateRoom(Room room) {
        Room existingRoom = roomRepository.findById(room.getId()).get();
        existingRoom.setName(room.getName());
        existingRoom.setFloorArea(room.getFloorArea());
        existingRoom.setWallArea(room.getWallArea());
        Room updateRoom = roomRepository.save(existingRoom);
        return updateRoom;
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
