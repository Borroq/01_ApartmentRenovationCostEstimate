package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomUpdateDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomResponseDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomSaveDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponseDto>> createRooms(@Valid @RequestBody RoomSaveDto room){
        RoomResponseDto savedRoom = roomService.createRoom(room);

        return new ResponseEntity<>(new ApiResponse<>("Room created successfully.", savedRoom), HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomById(@PathVariable("id") Long roomId){
        RoomResponseDto room = roomService.getRoomById(roomId);

        return new ResponseEntity<>(new ApiResponse<>("Room retrieved successfully", room), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoomResponseDto>>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RoomResponseDto> rooms = roomService.getAllRooms(pageable);

        return new ResponseEntity<>(new ApiResponse<>("Rooms retrieved successfully", rooms), HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(
            @Valid @PathVariable("id") Long roomId,
            @RequestBody RoomUpdateDto roomUpdateDto){

        roomUpdateDto.setId(roomId);
        RoomResponseDto updateRoom = roomService.updateRoom(roomUpdateDto);

        return new ResponseEntity<>(new ApiResponse<>("Room updated successfully", updateRoom), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable("id") Long roomId){
        roomService.deleteRoom(roomId);
        
        return new ResponseEntity<>(new ApiResponse<>("Room successfully deleted"), HttpStatus.OK);
    }
}
