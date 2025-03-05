package com.university.timetable.TimeTable.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class Room {
    @Id
    private String roomId;
    private String name;
    private int capacity;
    @DBRef
    RoomType roomType;
    private boolean isAvailable;
}
