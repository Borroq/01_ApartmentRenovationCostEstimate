package com.example.ApartmentRenovationCostEstimate.Services;

import com.example.ApartmentRenovationCostEstimate.entityDomain.Product;
import com.example.ApartmentRenovationCostEstimate.entityDomain.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(Room room);
    Room getRoomById(Integer roomId);
    List<Room> getAllRoom();
    Room updateRoom(Room room);
    void deleteRoom(Integer roomId);
}
