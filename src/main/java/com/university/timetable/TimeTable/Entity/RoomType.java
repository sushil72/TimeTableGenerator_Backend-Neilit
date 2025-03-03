package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class RoomType {
    private String id;
    private String name;
}
