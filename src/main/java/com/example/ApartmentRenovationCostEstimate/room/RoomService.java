package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.room.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(Room room);
    Room getRoomById(Integer roomId);
    List<Room> getAllRoom();
    Room updateRoom(Room room);
    void deleteRoom(Integer roomId);
}
