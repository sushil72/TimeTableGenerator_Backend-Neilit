package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class Program {
    @Id
    private String programId;
    private String title;
    private  boolean isActive;
}
