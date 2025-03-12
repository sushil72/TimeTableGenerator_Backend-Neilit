package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class RoomType {
    @Id
    private String id;
    private String name;
}
