package com.university.timetable.TimeTable.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;


@Document(collection = "daytimeslot")
@Setter
@Getter
public class DayTimeSlot {
    @Id
    private String daySlotId;
    private String SlotTitle;
    private String startTime;
    @DBRef
    private Days days;
    private String endTime;
    private boolean isActive;
}
