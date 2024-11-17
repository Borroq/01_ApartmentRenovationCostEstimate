package com.example.ApartmentRenovationCostEstimate.room;


import java.util.List;

public interface RoomService {
    Room createRoom(Room room);
    Room getRoomById(Long roomId);
    List<Room> getAllRoom();
    Room updateRoom(Room room);
    void deleteRoom(Long roomId);
}
