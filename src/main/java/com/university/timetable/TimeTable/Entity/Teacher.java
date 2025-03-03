package com.university.timetable.TimeTable.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "lecturer")
public class Teacher {

    @Id
    private String id;
    private String name;
    private  int contactNumber;
    private String isActive;
}
