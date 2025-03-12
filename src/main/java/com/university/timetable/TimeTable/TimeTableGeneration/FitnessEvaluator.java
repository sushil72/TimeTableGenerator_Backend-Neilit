package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FitnessEvaluator {

    public double evaluate(TimeTable timetable, List<TimeTable> allSchedules) {
        double score = 0.0;

        // ✅ Ensure timetable has required fields to prevent NullPointerException
        if (timetable == null) return score;

        // ✅ Assigning Room → Higher weight for correct assignments
        if (timetable.getRoom() != null) score += 10;

        // ✅ Assigning Lecturer & Subject
        if (timetable.getLecturerSubject() != null) score += 15;

        // ✅ Assigning Time Slot
        if (timetable.getDayTimeSlot() != null) score += 5;

        // 🚀 **Check for Lecturer Conflicts** (Same lecturer teaching multiple subjects at the same time)
        for (TimeTable other : allSchedules) {
            if (other != null &&
                    !timetable.equals(other) &&
                    timetable.getDayTimeSlot() != null &&
                    timetable.getLecturerSubject() != null &&
                    other.getDayTimeSlot() != null &&
                    other.getLecturerSubject() != null &&
                    timetable.getDayTimeSlot().equals(other.getDayTimeSlot()) &&
                    timetable.getLecturerSubject().getLecturer().equals(other.getLecturerSubject().getLecturer())) {
                score -= 20; // Heavy penalty for conflicts
            }
        }

        // 🚀 **Check for Room Conflicts** (Same room used for different lectures at the same time)
        for (TimeTable other : allSchedules) {
            if (other != null &&
                    !timetable.equals(other) &&
                    timetable.getDayTimeSlot() != null &&
                    timetable.getRoom() != null &&
                    other.getDayTimeSlot() != null &&
                    other.getRoom() != null &&
                    timetable.getDayTimeSlot().equals(other.getDayTimeSlot()) &&
                    timetable.getRoom().equals(other.getRoom())) {
                score -= 15; // Penalize room conflicts
            }
        }

        // 🚀 **Check Room Capacity** (Ensure students fit in the room)
        if (timetable.getRoom() != null && timetable.getProgramSemester() != null) {
            int roomCapacity = timetable.getRoom().getCapacity();
            int totalStudents = timetable.getProgramSemester().getTotalStudents();
            if (totalStudents > roomCapacity) {
                score -= 10; // Penalize for overcrowding
            }
        }

        // 🚀 **Balance Subject Distribution Across Days** (Avoid too many same subjects on the same day)
        Map<String, Long> subjectCounts = allSchedules.stream()
                .filter(tt -> tt.getProgramSemester() != null && tt.getProgramSemester().equals(timetable.getProgramSemester()))
                .filter(tt -> tt.getLecturerSubject() != null && tt.getLecturerSubject().getSubject() != null) // ✅ Handle null values
                .collect(Collectors.groupingBy(tt -> tt.getLecturerSubject().getSubject().getSubjectName(), Collectors.counting()));

        long subjectFrequency = subjectCounts.getOrDefault(
                timetable.getLecturerSubject() != null && timetable.getLecturerSubject().getSubject() != null
                        ? timetable.getLecturerSubject().getSubject().getSubjectName()
                        : "",
                0L);
        if (subjectFrequency > 3) {
            score -= 5; // Penalize if subject appears more than 3 times a week
        }

        // 🚀 **Prefer Consecutive Slots for Same Semester** (Reduce timetable gaps)
        for (TimeTable other : allSchedules) {
            if (other != null &&
                    timetable.getProgramSemester() != null &&
                    other.getProgramSemester() != null &&
                    timetable.getProgramSemester().equals(other.getProgramSemester()) &&
                    timetable.getDayTimeSlot() != null &&
                    other.getDayTimeSlot() != null &&
                    timetable.getDayTimeSlot().getDays() != null &&
                    other.getDayTimeSlot().getDays() != null &&
                    timetable.getDayTimeSlot().getDays().equals(other.getDayTimeSlot().getDays())) {

                // ✅ Extract numeric slot ID safely
                int currentSlot = timetable.getDayTimeSlot().getSlotTitle().replaceAll("\\D+", "").isEmpty()
                        ? -1 : Integer.parseInt(timetable.getDayTimeSlot().getSlotTitle().replaceAll("\\D+", ""));
                int otherSlot = other.getDayTimeSlot().getSlotTitle().replaceAll("\\D+", "").isEmpty()
                        ? -1 : Integer.parseInt(other.getDayTimeSlot().getSlotTitle().replaceAll("\\D+", ""));

                if (currentSlot != -1 && otherSlot != -1 && Math.abs(currentSlot - otherSlot) == 1) {
                    score += 10; // Reward consecutive classes
                }
            }
        }

        return score;
    }
}
