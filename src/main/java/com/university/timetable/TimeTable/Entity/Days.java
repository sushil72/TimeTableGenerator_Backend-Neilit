package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "days")
public class Days {
    private String dayId;
    private String dayName;
    private boolean isActive;
}
