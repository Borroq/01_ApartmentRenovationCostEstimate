package com.example.ApartmentRenovationCostEstimate.services;

import com.example.ApartmentRenovationCostEstimate.entity.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(Room room);
    Room getRoomById(Integer roomId);
    List<Room> getAllRoom();
    Room updateRoom(Room room);
    void deleteRoom(Integer roomId);
}
