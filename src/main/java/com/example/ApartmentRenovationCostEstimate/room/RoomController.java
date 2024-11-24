package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.room.DTOs.RoomDto;
import com.example.ApartmentRenovationCostEstimate.room.DTOs.RoomSaveDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/rooms")
public class RoomController {

    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    //Create Room - REST API
    @PostMapping
    public ResponseEntity<Object> createRooms(@Valid @RequestBody RoomSaveDto room){
        Room savedRoom = roomService.createRoom(room);
        return new ResponseEntity<>(new ApiResponse<>("Room created successfully.", savedRoom), HttpStatus.CREATED);
    }

    //Get Room by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("id") Long roomId){
        RoomDto room = roomService.getRoomById(roomId);

        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    //Get all Rooms - REST API
    @GetMapping
    public ResponseEntity<Object> getAllRooms(){
        List<RoomDto> rooms = roomService.getAllRoom();
        if (rooms.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("No rooms found"), HttpStatus.OK);
        }

        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    //Update Room by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Object> updateRoom(@Valid @PathVariable("id") Long roomId, @RequestBody RoomDto roomDto){

        roomDto.setId(roomId);
        Room updateRoom = roomService.updateRoom(roomDto);

        return new ResponseEntity<>(new ApiResponse<>("Room updated successfully", updateRoom), HttpStatus.OK);
    }

    //Delete Room by Id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteRoom(@PathVariable("id") Long roomId){

        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(new ApiResponse<>("Room successfully deleted"), HttpStatus.OK);
    }
}
