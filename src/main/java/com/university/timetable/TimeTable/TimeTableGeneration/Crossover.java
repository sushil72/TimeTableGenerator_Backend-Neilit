package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;

public class Crossover {
    public TimeTable crossover(TimeTable parent1, TimeTable parent2) {
        TimeTable child = new TimeTable();

        // Randomly inherit attributes from parents
        child.setRoom(Math.random() > 0.5 ? parent1.getRoom() : parent2.getRoom());
        child.setLecturerSubject(Math.random() > 0.5 ? parent1.getLecturerSubject() : parent2.getLecturerSubject());
        child.setDayTimeSlot(Math.random() > 0.5 ? parent1.getDayTimeSlot() : parent2.getDayTimeSlot());

        return child;
    }

}
