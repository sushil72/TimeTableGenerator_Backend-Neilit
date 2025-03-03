package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.DayTimeSlot;
import com.university.timetable.TimeTable.Entity.Room;
import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Services.DayTimeSlotService;

public class Mutation {
    public void mutate(TimeTable timetable) {
        if (Math.random() < 0.1) { // 10% chance of mutation
            timetable.setRoom(new Room()); // Assign a different room
        }
        if (Math.random() < 0.1) {
            timetable.setDayTimeSlot(new DayTimeSlot()); // Assign different slot
        }
    }
}
