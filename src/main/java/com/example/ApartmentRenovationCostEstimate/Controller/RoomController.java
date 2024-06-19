package com.example.ApartmentRenovationCostEstimate.Controller;

import com.example.ApartmentRenovationCostEstimate.Services.RoomService;
import com.example.ApartmentRenovationCostEstimate.entityDomain.Room;
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
    public ResponseEntity<Room> createRooms(@RequestBody Room room){
        Room savedRoom = roomService.createRoom(room);
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    //Get Room by ID - REST API
    @GetMapping("{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") Integer roomId){
        Room room = roomService.getRoomById(roomId);
        return new ResponseEntity<>(room,HttpStatus.OK);
    }

    //Get all Rooms - REST API
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = roomService.getAllRoom();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    //Update Room by Id - REST API
    @PutMapping("{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable("id") Integer roomId, @RequestBody Room room){
        room.setId(roomId);
        Room updateRoom = roomService.updateRoom(room);
        return new ResponseEntity<>(updateRoom, HttpStatus.OK);
    }

    //Delete Room by Id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Integer roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>("Room succesfully deleted", HttpStatus.OK);
    }
}
