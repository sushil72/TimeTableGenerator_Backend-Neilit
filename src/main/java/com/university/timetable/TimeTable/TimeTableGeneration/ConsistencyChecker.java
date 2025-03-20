package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.DayTimeSlot;
import com.university.timetable.TimeTable.Entity.ProgramSemester;
import com.university.timetable.TimeTable.Entity.TimeTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This utility class checks for consistency and constraint violations in a timetable
 */

@Component
@Slf4j
public class ConsistencyChecker {

    /**
     * Performs a complete check of all constraints on a timetable
     *
     * @param timetable The timetable to validate
     * @return List of constraint violation messages
     */
    public List<String> checkAllConstraints(List<TimeTable> timetable) {
        List<String> violations = new ArrayList<>();

        violations.addAll(checkLecturerConflicts(timetable));
        violations.addAll(checkRoomConflicts(timetable));
        violations.addAll(checkClassConflicts(timetable));
        violations.addAll(checkRoomCapacity(timetable));
        // Add more constraint checks as needed

        return violations;
    }

    /**
     * Checks if any lecturer is scheduled for multiple classes at the same time
     */

    public List<String> checkLecturerConflicts(List<TimeTable> timetable) {
        List<String> violations = new ArrayList<>();

        // Group by timeslot
        Map<String, List<TimeTable>> slotMap = timetable.stream()
                .collect(Collectors.groupingBy(entry -> entry.getDayTimeSlot().getDaySlotId()));

        // For each timeslot, check if any lecturer appears more than once
        for (Map.Entry<String, List<TimeTable>> slotEntry : slotMap.entrySet()) {
            List<TimeTable> entriesInSlot = slotEntry.getValue();

            // Group by lecturer
            Map<String, List<TimeTable>> lecturerMap = entriesInSlot.stream()
                    .collect(Collectors.groupingBy(entry -> entry.getLecturerSubject().getLecturer().getId()));

            for (Map.Entry<String, List<TimeTable>> lecturerEntry : lecturerMap.entrySet()) {
                if (lecturerEntry.getValue().size() > 1) {
                    // Conflict found
                    String lecturerName = lecturerEntry.getValue().get(0)
                            .getLecturerSubject().getLecturer().getName();
                    String slotInfo = getSlotInfo(lecturerEntry.getValue().get(0).getDayTimeSlot());

                    violations.add(String.format(
                            "Lecturer conflict: %s is assigned to %d classes at %s",
                            lecturerName, lecturerEntry.getValue().size(), slotInfo));
                }
            }
        }

        return violations;
    }

    /**
     * Checks if any room is scheduled for multiple classes at the same time
     */
    public List<String> checkRoomConflicts(List<TimeTable> timetable) {
        List<String> violations = new ArrayList<>();

        // Group by timeslot
        Map<String, List<TimeTable>> slotMap = timetable.stream()
                .collect(Collectors.groupingBy(entry -> entry.getDayTimeSlot().getDaySlotId()));

        // For each timeslot, check if any room appears more than once
        for (Map.Entry<String, List<TimeTable>> slotEntry : slotMap.entrySet()) {
            List<TimeTable> entriesInSlot = slotEntry.getValue();

            // Group by room
            Map<String, List<TimeTable>> roomMap = entriesInSlot.stream()
                    .collect(Collectors.groupingBy(entry -> entry.getRoom().getRoomId()));

            for (Map.Entry<String, List<TimeTable>> roomEntry : roomMap.entrySet()) {
                if (roomEntry.getValue().size() > 1) {
                    // Conflict found
                    String roomName = roomEntry.getValue().get(0).getRoom().getName();
                    String slotInfo = getSlotInfo(roomEntry.getValue().get(0).getDayTimeSlot());

                    violations.add(String.format(
                            "Room conflict: %s is assigned to %d classes at %s",
                            roomName, roomEntry.getValue().size(), slotInfo));
                }
            }
        }

        return violations;
    }

    /**
     * Checks if any class/semester is scheduled for multiple subjects at the same time
     */
    public List<String> checkClassConflicts(List<TimeTable> timetable) {
        List<String> violations = new ArrayList<>();

        // Group by timeslot
        Map<String, List<TimeTable>> slotMap = timetable.stream()
                .collect(Collectors.groupingBy(entry -> entry.getDayTimeSlot().getDaySlotId()));

        // For each timeslot, check if any semester appears more than once
        for (Map.Entry<String, List<TimeTable>> slotEntry : slotMap.entrySet()) {
            List<TimeTable> entriesInSlot = slotEntry.getValue();

            // Group by semester
            Map<String, List<TimeTable>> semesterMap = entriesInSlot.stream()
                    .collect(Collectors.groupingBy(entry -> entry.getProgramSemester().getProgramSemesterId()));

            for (Map.Entry<String, List<TimeTable>> semesterEntry : semesterMap.entrySet()) {
                if (semesterEntry.getValue().size() > 1) {
                    // Conflict found
                    String semesterName = getSemesterInfo(semesterEntry.getValue().get(0).getProgramSemester());
                    String slotInfo = getSlotInfo(semesterEntry.getValue().get(0).getDayTimeSlot());

                    violations.add(String.format(
                            "Class conflict: %s is assigned to %d subjects at %s",
                            semesterName, semesterEntry.getValue().size(), slotInfo));
                }
            }
        }

        return violations;
    }

    /**
     * Checks if any room is assigned to a class that exceeds its capacity
     */
    public List<String> checkRoomCapacity(List<TimeTable> timetable) {
        List<String> violations = new ArrayList<>();

        for (TimeTable entry : timetable) {
            int classSize = 60;
            int roomCapacity = entry.getRoom().getCapacity();

            if (classSize > roomCapacity) {
                String semesterInfo = getSemesterInfo(entry.getProgramSemester());
                String roomName = entry.getRoom().getName();
                String subjectName = entry.getLecturerSubject().getSubject().getSubjectName();

                violations.add(String.format(
                        "Room capacity exceeded: %s (%d students) assigned to %s (capacity %d) for %s",
                        semesterInfo, classSize, roomName, roomCapacity, subjectName));
            }
        }

        return violations;
    }

    /**
     * Helper methods to format information
     */
    private String getSlotInfo(DayTimeSlot slot) {
        return slot.getDays().getDayName() + " " + slot.getStartTime() + "-" + slot.getEndTime();
    }

    private String getSemesterInfo(ProgramSemester semester) {
        return semester.getProgram().getTitle() + " Semester " + semester.getSemester();
    }
}