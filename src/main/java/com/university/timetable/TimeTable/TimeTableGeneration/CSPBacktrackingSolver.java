package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.*;
import com.university.timetable.TimeTable.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSPBacktrackingSolver {
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;
    private final ConstraintDomain constraintDomain; // Inject the renamed class

    private List<TimeTable> bestSolution = new ArrayList<>();
    private int backtrackCount = 0;
    private int conflictCount = 0;

    /**
     * Solves the timetable generation problem using CSP with backtracking
     */
    public List<TimeTable> solve() {
        log.info("Starting CSP solver with backtracking...");

        List<DayTimeSlot> slots = dayTimeSlotRepository.findAll();
        List<ProgramSemester> semesters = programSemesterRepository.findAll();

        // Reset counters
        backtrackCount = 0;
        conflictCount = 0;
        bestSolution.clear();

        // Initialize the constraint domain if needed
        constraintDomain.initializeDomainConstraints();

        // Create variables (semester-timeslot pairs)
        List<Variable> variables = new ArrayList<>();
        for (ProgramSemester semester : semesters) {
            // Get required subjects for this semester
            List<LecturerSubject> requiredSubjects = getRequiredSubjectsForSemester(semester);

            for (LecturerSubject subject : requiredSubjects) {
                Variable var = new Variable(semester, subject);
                variables.add(var);
            }
        }

        // Sort variables by MRV heuristic (most constrained first)
        variables.sort(Comparator.comparingInt(v ->
                getDomainSize(v.semester, v.subject)));

        // Apply backtracking with the ordered variables
        List<Assignment> assignments = new ArrayList<>();
        boolean success = backtrack(variables, assignments, 0);

        if (success) {
            log.info("Solution found after {} backtracks and {} conflicts",
                    backtrackCount, conflictCount);

            // Convert assignments to timetable entries
            List<TimeTable> solution = convertToTimetable(assignments);
            return solution;
        } else {
            log.error("No solution found after {} attempts", backtrackCount);
            throw new RuntimeException("❌ No valid timetable found!");
        }
    }

    /**
     * Main backtracking algorithm
     */
    private boolean backtrack(List<Variable> variables, List<Assignment> assignments, int index) {
        if (index == variables.size()) {
            // All variables assigned successfully
            return true;
        }

        backtrackCount++;
        if (backtrackCount % 1000 == 0) {
            log.info("Backtracking... Attempts: {}, Current depth: {}/{}",
                    backtrackCount, index, variables.size());
        }

        Variable var = variables.get(index);
        List<LecturerSubject> subjects = Collections.singletonList(var.subject);

        // Get available slots for this variable
        List<DayTimeSlot> availableSlots = getAvailableSlots(var.semester);
        // Order by least constraining value heuristic
        sortByLeastConstrainingValue(availableSlots, var, assignments);

        for (DayTimeSlot slot : availableSlots) {
            // Get suitable rooms
            List<Room> suitableRooms = constraintDomain.getSuitableRoomsForSubject(
                    var.subject, 60); // numberof Students in a semester
            for (Room room : suitableRooms) {
                Assignment assignment = new Assignment(var.semester, var.subject, slot, room);

                if (isValidAssignment(assignment, assignments)) {
                    // Try this assignment
                    assignments.add(assignment);

                    // Recur to next variable
                    if (backtrack(variables, assignments, index + 1)) {
                        return true;
                    }

                    // If we get here, this assignment didn't work
                    assignments.remove(assignments.size() - 1);
                } else {
                    conflictCount++;
                }
            }
        }

        return false; // No valid assignment found for this variable
    }

    /**
     * Check if an assignment violates any constraints
     */
    private boolean isValidAssignment(Assignment newAssignment, List<Assignment> currentAssignments) {
        DayTimeSlot newSlot = newAssignment.slot;
        Room newRoom = newAssignment.room;
        LecturerSubject newSubject = newAssignment.subject;

        for (Assignment existing : currentAssignments) {
            // Skip incomparable assignments
            if (existing.slot == null || existing.room == null || existing.subject == null) {
                continue;
            }

            // Check time slot conflicts
            if (existing.slot.equals(newSlot)) {
                // Room conflict - same room at same time
                if (existing.room.equals(newRoom)) {
                    return false;
                }

                // Lecturer conflict - same lecturer at same time
                if (existing.subject.getLecturer().getId().equals(
                        newSubject.getLecturer().getId())) {
                    return false;
                }

                // Class conflict - same semester at same time
                if (existing.semester.equals(newAssignment.semester)) {
                    return false;
                }
            }

            // Check consecutive sessions for same subject
            if (existing.subject.equals(newSubject) &&
                    areSlotsConsecutive(existing.slot, newSlot)) {
                // Allow consecutive sessions for labs or as needed
                // This is a placeholder - modify based on requirements
                // return false;
            }
        }

        return true;
    }

    /**
     * Convert assignments to TimeTable objects
     */
    private List<TimeTable> convertToTimetable(List<Assignment> assignments) {
        return assignments.stream().map(a -> {
            TimeTable entry = new TimeTable();
            entry.setProgramSemester(a.semester);
            entry.setLecturerSubject(a.subject);
            entry.setDayTimeSlot(a.slot);
            entry.setRoom(a.room);
            return entry;
        }).collect(Collectors.toList());
    }

    /**
     * Helper methods
     */
    private List<LecturerSubject> getRequiredSubjectsForSemester(ProgramSemester semester) {
        // This should be implemented based on your data model
        // How subjects are associated with semesters in your system
        return constraintDomain.getAvailableSubjectsForSemester(semester);
    }

    private List<DayTimeSlot> getAvailableSlots(ProgramSemester semester) {
        // Get all slots or filter based on semester requirements
        return dayTimeSlotRepository.findAll();
    }

    private int getDomainSize(ProgramSemester semester, LecturerSubject subject) {
        // Calculate domain size (number of possible slot-room combinations)
        List<DayTimeSlot> slots = getAvailableSlots(semester);
        List<Room> rooms = constraintDomain.getSuitableRoomsForSubject(
                subject, 60); // numberof Students in a semester
        return slots.size() * rooms.size();
    }

    private void sortByLeastConstrainingValue(List<DayTimeSlot> slots,
                                              Variable var, List<Assignment> currentAssignments) {
        // Sort slots by how many conflicts they would cause
        // This is a placeholder - implement based on your needs
        Collections.shuffle(slots); // For now, just randomize
    }

    private boolean areSlotsConsecutive(DayTimeSlot slot1, DayTimeSlot slot2) {
        // Check if slots are on same day and consecutive times
        // This is a placeholder - implement based on your data model
        return false;
    }

    /**
     * Helper classes
     */
    private static class Variable {
        ProgramSemester semester;
        LecturerSubject subject;

        public Variable(ProgramSemester semester, LecturerSubject subject) {
            this.semester = semester;
            this.subject = subject;
        }
    }

    private static class Assignment {
        ProgramSemester semester;
        LecturerSubject subject;
        DayTimeSlot slot;
        Room room;

        public Assignment(ProgramSemester semester, LecturerSubject subject,
                          DayTimeSlot slot, Room room) {
            this.semester = semester;
            this.subject = subject;
            this.slot = slot;
            this.room = room;
        }
    }
}