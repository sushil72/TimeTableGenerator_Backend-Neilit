package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.*;
import com.university.timetable.TimeTable.Repository.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class ConstraintDomain {  // Renamed from Population to better reflect purpose
    private final List<TimeTable> possibleAssignments = new ArrayList<>();
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;

    public ConstraintDomain(DayTimeSlotRepository dayTimeSlotRepository,
                            ProgramSemesterRepository programSemesterRepository,
                            LecturerSubjectRepository lecturerSubjectRepository,
                            RoomRepository roomRepository) {
        this.dayTimeSlotRepository = dayTimeSlotRepository;
        this.programSemesterRepository = programSemesterRepository;
        this.lecturerSubjectRepository = lecturerSubjectRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Initializes the domain with filtered constraints based on semester requirements
     */
    public void initializeDomainConstraints() {
        List<DayTimeSlot> slots = dayTimeSlotRepository.findAll();
        List<ProgramSemester> semesters = programSemesterRepository.findAll();
        List<LecturerSubject> subjects = lecturerSubjectRepository.findAll();
        List<Room> rooms = roomRepository.findAll();

        if (slots.isEmpty() || semesters.isEmpty() || subjects.isEmpty() || rooms.isEmpty()) {
            throw new IllegalStateException("❌ Missing data for timetable generation!");
        }

        // Clear previous assignments if any
        possibleAssignments.clear();

        // Only create valid combinations based on semester-subject relationships
        for (ProgramSemester semester : semesters) {
            // Filter subjects relevant to this semester
            List<LecturerSubject> semesterSubjects = subjects.stream()
                    .filter(subject -> isSubjectValidForSemester(subject, semester))
                    .toList();

            for (LecturerSubject subject : semesterSubjects) {
                for (DayTimeSlot slot : slots) {
                    // Filter suitable rooms
                    List<Room> suitableRooms = rooms.stream()
                            .filter(room -> isRoomSuitable(room, subject, 60)) //number of students in a semester
                            .toList();

                    for (Room room : suitableRooms) {
                        TimeTable timetableEntry = new TimeTable();
                        timetableEntry.setProgramSemester(semester);
                        timetableEntry.setLecturerSubject(subject);
                        timetableEntry.setDayTimeSlot(slot);
                        timetableEntry.setRoom(room);
                        possibleAssignments.add(timetableEntry);
                    }
                }
            }
        }

        System.out.println("✅ Constraint domains initialized: " + possibleAssignments.size() + " possible assignments.");
    }

    /**
     * Checks if a subject is valid for a given semester based on curriculum
     */
    private boolean isSubjectValidForSemester(LecturerSubject subject, ProgramSemester semester) {
        // This is a placeholder - implement based on your data model
        // Example: return subject.getSubject().getProgramSemester().equals(semester);
        return true; // Temporary default
    }

    /**
     * Checks if a room is suitable for a subject and class size
     */
    private boolean isRoomSuitable(Room room, LecturerSubject subject, int classSize) {
        // Check room capacity
        if (room.getCapacity() < classSize) {
            return false;
        }

        // This is a placeholder - implement based on your data model
        // Example: if (subject.getSubject().requiresLab() && !room.isLabRoom()) return false;

        return true; // Room is suitable
    }

    public List<TimeTable> getPossibleAssignments() {
        if (possibleAssignments.isEmpty()) {
            initializeDomainConstraints();
        }
        return possibleAssignments;
    }

    /**
     * Gets available subjects for a specific semester
     */
    public List<LecturerSubject> getAvailableSubjectsForSemester(ProgramSemester semester) {
        List<LecturerSubject> subjects = lecturerSubjectRepository.findAll();
        return subjects.stream()
                .filter(subject -> isSubjectValidForSemester(subject, semester))
                .toList();
    }

    /**
     * Gets suitable rooms for a specific subject and class size
     */
    public List<Room> getSuitableRoomsForSubject(LecturerSubject subject, int classSize) {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .filter(room -> isRoomSuitable(room, subject, classSize))
                .toList();
    }
}