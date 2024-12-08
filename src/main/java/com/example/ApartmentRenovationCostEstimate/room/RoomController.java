package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomDto;
import com.example.ApartmentRenovationCostEstimate.room.dtos.RoomSaveDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @PostMapping
    public ResponseEntity<Object> createRooms(@Valid @RequestBody RoomSaveDto room){
        Room savedRoom = roomService.createRoom(room);

        return new ResponseEntity<>(new ApiResponse<>("Room created successfully.", savedRoom), HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("id") Long roomId){
        RoomDto room = roomService.getRoomById(roomId);

        return new ResponseEntity<>(room, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Object> getAllRooms(){
        List<RoomDto> rooms = roomService.getAllRoom();
        if (rooms.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No rooms found"), HttpStatus.OK);
        }

        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<Object> updateRoom(@Valid @PathVariable("id") Long roomId, @RequestBody RoomDto roomDto){

        roomDto.setId(roomId);
        Room updateRoom = roomService.updateRoom(roomDto);

        return new ResponseEntity<>(new ApiResponse<>("Room updated successfully", updateRoom), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteRoom(@PathVariable("id") Long roomId){

        roomService.deleteRoom(roomId);
        
        return new ResponseEntity<>(new ApiResponse<>("Room successfully deleted"), HttpStatus.OK);
    }
}
