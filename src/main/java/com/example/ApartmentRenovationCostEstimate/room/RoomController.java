package com.example.ApartmentRenovationCostEstimate.room;

import com.example.ApartmentRenovationCostEstimate.response.ApiResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorResponse;
import com.example.ApartmentRenovationCostEstimate.response.ErrorType;
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
    public ResponseEntity<Object> createRooms(@RequestBody Room room){
        Room savedRoom = roomService.createRoom(room);
        return new ResponseEntity<>(new ApiResponse<>("Product created successfully.", savedRoom), HttpStatus.CREATED);
    }

    //Get Room by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<Object> getRoomById(@PathVariable("id") Long roomId){
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.ROOM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse<>("Room retrieved successfully", room),HttpStatus.OK);
    }

    //Get all Rooms - REST API
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = roomService.getAllRoom();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    //Update Room by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Object> updateRoom(@PathVariable("id") Long roomId, @RequestBody Room room){
        Room existingRoom = roomService.getRoomById(roomId);
        if (existingRoom == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.ROOM_NOT_FOUND),HttpStatus.NOT_FOUND);
        }

        room.setId(roomId);
        Room updateRoom = roomService.updateRoom(room);

        return new ResponseEntity<>(new ApiResponse<>("Room updated successfully", updateRoom), HttpStatus.OK);
    }

    //Delete Room by Id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteRoom(@PathVariable("id") Long roomId){
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            return new ResponseEntity<>(new ErrorResponse(ErrorType.ROOM_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(new ApiResponse<>("Room succesfully deleted"), HttpStatus.OK);
    }
}
