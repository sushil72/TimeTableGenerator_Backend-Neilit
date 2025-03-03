package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Room;
import com.university.timetable.TimeTable.Entity.RoomType;
import com.university.timetable.TimeTable.Services.RoomService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/allRooms")
    public ResponseEntity<List<Room>> getRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/addRoom")
    public ResponseEntity<Room> addRoom(@RequestBody  Room room) {
    return roomService.addRoom(room);
    }

    @PostMapping("/room-type")
    public ResponseEntity<RoomType> addRoomType(@RequestBody RoomType roomType) {
        return roomService.addRoomType(roomType);
    }

}
