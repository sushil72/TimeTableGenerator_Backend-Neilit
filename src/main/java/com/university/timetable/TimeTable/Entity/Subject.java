package com.university.timetable.TimeTable.Entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subject")
public class Subject {
    @Id
    private String id;
    private String subjectName;
    private String code;
    private boolean isActive;
    private int crsHrs;
}
