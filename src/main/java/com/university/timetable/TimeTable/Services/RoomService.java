package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Room;
import com.university.timetable.TimeTable.Entity.RoomType;
import com.university.timetable.TimeTable.Repository.RoomRepository;
import com.university.timetable.TimeTable.Repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository  roomRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;

    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    public ResponseEntity<Room> addRoom(Room room) {
        Optional<Room> Dbr = roomRepository.findById(room.getRoomId());
        if(Dbr.isPresent()) {
            throw  new IllegalStateException("Room already exists");
        }
        Room newRoom = new Room();
        newRoom.setRoomId(room.getRoomId());
        newRoom.setName(room.getName());
        newRoom.setCapacity(room.getCapacity());
        newRoom.setAvailable(room.isAvailable());
        newRoom.setRoomType(room.getRoomType());
        return ResponseEntity.ok(roomRepository.save(newRoom));
    }

    public ResponseEntity<RoomType> addRoomType(RoomType roomType) {
        Optional<RoomType> dbRoomType = roomTypeRepository.findById(roomType.getId());
        if(dbRoomType.isPresent()) {
            throw  new IllegalStateException("Room type already exists");
        }
        RoomType newRoomType = new RoomType();
        newRoomType.setId(roomType.getId());
        newRoomType.setName(roomType.getName());
              return new ResponseEntity<>(roomTypeRepository.save(newRoomType), HttpStatus.CREATED);
    }
}
