package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;

import java.util.List;

public class FitnessEvaluator {

    public double evaluate(TimeTable timetable, List<TimeTable> allSchedules) {
        double score = 0.0;

        // ✅ Assigning Room → Higher weight for correct assignments
        if (timetable.getRoom() != null) score += 10;

        // ✅ Assigning Lecturer & Subject
        if (timetable.getLecturerSubject() != null) score += 15;

        // ✅ Assigning Time Slot
        if (timetable.getDayTimeSlot() != null) score += 5;

        // 🚀 **Check for Lecturer Conflicts** (Lecturer teaching two subjects at the same time)
        for (TimeTable other : allSchedules) {
            if (!timetable.equals(other) &&
                    timetable.getDayTimeSlot().equals(other.getDayTimeSlot()) &&
                    timetable.getLecturerSubject().getLecturer().equals(other.getLecturerSubject().getLecturer())) {
                score -= 20; // Heavy penalty for conflicts
            }
        }

        // **Check for Room Conflicts** (Same room used for different lectures at the same time)
        for (TimeTable other : allSchedules) {
            if (!timetable.equals(other) &&
                    timetable.getDayTimeSlot().equals(other.getDayTimeSlot()) &&
                    timetable.getRoom().equals(other.getRoom())) {
                score -= 15; // Penalize room conflicts
            }
        }

        // **Check Room Capacity** (Ensure students fit in the room)
        if (timetable.getRoom() != null && timetable.getProgramSemester() != null) {
            int roomCapacity = timetable.getRoom().getCapacity();
            int totalStudents = timetable.getProgramSemester().getTotalStudents();
            if (totalStudents > roomCapacity) {
                score -= 10; // Penalize for overcrowding
            }
        }

        return score;
    }
}
