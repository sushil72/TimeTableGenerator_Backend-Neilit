package com.university.timetable.TimeTable.TimeTableGeneration;


import com.university.timetable.TimeTable.Entity.*;
import com.university.timetable.TimeTable.Repository.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    private List<TimeTable> timetables;
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;
    private final Random random = new Random();

    // Constructor that initializes a population of timetables
    public Population(int size,
                      DayTimeSlotRepository dayTimeSlotRepo,
                      ProgramSemesterRepository programSemesterRepo,
                      LecturerSubjectRepository lecturerSubjectRepo,
                      RoomRepository roomRepo) {

        this.dayTimeSlotRepository = dayTimeSlotRepo;
        this.programSemesterRepository = programSemesterRepo;
        this.lecturerSubjectRepository = lecturerSubjectRepo;
        this.roomRepository = roomRepo;

        timetables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            timetables.add(generateRandomTimetable());
        }
    }

    private TimeTable generateRandomTimetable() {
        TimeTable timetable = new TimeTable();

        // Fetch available data from repositories
        List<DayTimeSlot> dayTimeSlots = dayTimeSlotRepository.findAll();
        List<ProgramSemester> programSemesters = programSemesterRepository.findAll();
        List<LecturerSubject> lecturerSubjects = lecturerSubjectRepository.findAll();
        List<Room> rooms = roomRepository.findAll();

        // Ensure data exists before generating a schedule
        if (dayTimeSlots.isEmpty() || programSemesters.isEmpty() || lecturerSubjects.isEmpty() || rooms.isEmpty()) {
            throw new IllegalStateException("No data found in the database for scheduling!");
        }

        // Assign random values from the available options
        timetable.setDayTimeSlot(dayTimeSlots.get(random.nextInt(dayTimeSlots.size())));
        timetable.setProgramSemester(programSemesters.get(random.nextInt(programSemesters.size())));
        timetable.setLecturerSubject(lecturerSubjects.get(random.nextInt(lecturerSubjects.size())));
        timetable.setRoom(rooms.get(random.nextInt(rooms.size())));

        // Generate a random start and end time (between 9 AM - 5 PM)
        timetable.setStartTime(generateRandomStartTime());
        timetable.setEndTime(timetable.getStartTime().plusHours(1)); // Assuming 1-hour lectures

        return timetable;
    }

    private LocalTime generateRandomStartTime() {
        // Possible time slots for lectures (9 AM - 5 PM)
        LocalTime[] timeSlots = {
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        };
        return timeSlots[random.nextInt(timeSlots.length)];
    }

    public List<TimeTable> getTimetables() {
        return timetables;
    }
}
