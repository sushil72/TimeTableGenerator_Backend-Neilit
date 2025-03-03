package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "sessions")
public class Sessions {
    private String sessionId;
    private String sessionName;
    private boolean isActive;
}
