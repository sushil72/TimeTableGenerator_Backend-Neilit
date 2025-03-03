package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "Labs")
public class Lab {
    private String labId;
    private String labName;
    private String labNumber;
    private int capacity;
    private boolean isActive;
}
