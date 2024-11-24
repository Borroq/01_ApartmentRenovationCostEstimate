package com.example.ApartmentRenovationCostEstimate.room;


import com.example.ApartmentRenovationCostEstimate.room.DTOs.RoomDto;
import com.example.ApartmentRenovationCostEstimate.room.DTOs.RoomSaveDto;

import java.util.List;

public interface RoomService {
    Room createRoom(RoomSaveDto roomSaveDto);
    RoomDto getRoomById(Long roomId);
    List<RoomDto> getAllRoom();
    Room updateRoom(RoomDto roomDto);
    void deleteRoom(Long roomId);
}
