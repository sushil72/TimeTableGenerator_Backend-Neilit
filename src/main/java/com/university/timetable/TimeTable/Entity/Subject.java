package com.university.timetable.TimeTable.Entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Subject {
    @Id
    private String id;
    private String name;
    private boolean isActive;
    private int crsHrs;
    @DBRef
    private RoomType roomType;
}
