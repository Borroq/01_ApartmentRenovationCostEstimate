package com.example.ApartmentRenovationCostEstimate.Services;

import com.example.ApartmentRenovationCostEstimate.entityDomain.Product;
import com.example.ApartmentRenovationCostEstimate.entityDomain.Room;
import com.example.ApartmentRenovationCostEstimate.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RoomServiceImplement implements RoomService{
    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImplement(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Integer roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        return optionalRoom.get();
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
    public void deleteRoom(Integer roomId) {
        roomRepository.deleteById(roomId);
    }
}
