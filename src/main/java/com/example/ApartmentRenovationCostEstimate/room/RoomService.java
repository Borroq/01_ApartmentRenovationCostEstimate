package com.example.ApartmentRenovationCostEstimate.room;


import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomUpdateDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomResponseDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomSaveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    RoomResponseDto createRoom(RoomSaveDto roomSaveDto);
    RoomResponseDto getRoomById(Long roomId);
    Page<RoomResponseDto> getAllRooms(Pageable pageable);
    RoomResponseDto updateRoom(RoomUpdateDto roomUpdateDto);
    void deleteRoom(Long roomId);
}
