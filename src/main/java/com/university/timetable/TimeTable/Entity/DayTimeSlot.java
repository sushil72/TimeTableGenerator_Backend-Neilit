package com.university.timetable.TimeTable.Entity;

import com.university.timetable.TimeTable.Services.DayTimeSlotService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Document(collection = "daytimeslot")
@Setter
@Getter
public class DayTimeSlot {
    private String dayId;
    private String SlotTitle;
    private String startTime;
    @DBRef
    private Days days;
    private String endTime;
    private boolean isActive;


}
