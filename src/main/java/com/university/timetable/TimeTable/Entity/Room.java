package com.university.timetable.TimeTable.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roles")
public class Room {
    @Id
    private String roomId;
    private String roomNumber;
    private String Capacity;
    @DBRef
    RoomType roomType;
    private boolean isAvailable;
}
