package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class Program {
    private String programId;
    private String Title;
    private  boolean isActive;
}
