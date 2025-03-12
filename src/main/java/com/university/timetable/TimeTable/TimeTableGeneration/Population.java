package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.*;
import com.university.timetable.TimeTable.Repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;
import java.time.LocalTime;

@Component
@Data
public class Population {
    private final int populationSize;
    private List<List<TimeTable>> population = new ArrayList<>();
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;
    private final Random random = new Random();

    public Population(@Value("${population.size:20}") int populationSize,
                      DayTimeSlotRepository dayTimeSlotRepository,
                      ProgramSemesterRepository programSemesterRepository,
                      LecturerSubjectRepository lecturerSubjectRepository,
                      RoomRepository roomRepository) {
        this.populationSize = populationSize;
        this.dayTimeSlotRepository = dayTimeSlotRepository;
        this.programSemesterRepository = programSemesterRepository;
        this.lecturerSubjectRepository = lecturerSubjectRepository;
        this.roomRepository = roomRepository;
        initializePopulation();
    }

    private void initializePopulation() {
        List<DayTimeSlot> slots = dayTimeSlotRepository.findAll();
        List<ProgramSemester> semesters = programSemesterRepository.findAll();
        List<LecturerSubject> subjects = lecturerSubjectRepository.findAll();
        List<Room> rooms = roomRepository.findAll();

        if (slots.isEmpty() || semesters.isEmpty() || subjects.isEmpty() || rooms.isEmpty()) {
            throw new IllegalStateException("❌ Missing data for timetable generation!");
        }

        // Create multiple candidate solutions (individuals in the population)
        for (int i = 0; i < populationSize; i++) {
            List<TimeTable> individualTimetable = createRandomTimetable(slots, semesters, subjects, rooms);
            population.add(individualTimetable);
        }
    }

    private List<TimeTable> createRandomTimetable(List<DayTimeSlot> slots,
                                                  List<ProgramSemester> semesters,
                                                  List<LecturerSubject> subjects,
                                                  List<Room> rooms) {
        List<TimeTable> timetable = new ArrayList<>();

        // Maps to track resource usage to prevent double booking
        Map<String, Boolean> roomBookings = new HashMap<>();
        Map<String, Boolean> lecturerBookings = new HashMap<>();
        Map<String, Boolean> semesterBookings = new HashMap<>();

        // Determine subject requirements for each semester
        Map<ProgramSemester, List<LecturerSubject>> semesterSubjectRequirements = new HashMap<>();

        // In a real implementation, this would be fetched from your database
        // For now, let's assign 4-8 random subjects to each semester
        for (ProgramSemester semester : semesters) {
            // Randomly select 4-8 subjects for this semester
            int requiredSubjects = 4 + random.nextInt(5);
            List<LecturerSubject> semesterSubjects = new ArrayList<>();

            // Randomly assign subjects to this semester
            List<LecturerSubject> shuffledSubjects = new ArrayList<>(subjects);
            Collections.shuffle(shuffledSubjects);
            for (int i = 0; i < Math.min(requiredSubjects, shuffledSubjects.size()); i++) {
                semesterSubjects.add(shuffledSubjects.get(i));
            }

            semesterSubjectRequirements.put(semester, semesterSubjects);
        }

        // For each semester, schedule all required subjects
        for (ProgramSemester semester : semesters) {
            List<LecturerSubject> requiredSubjects = semesterSubjectRequirements.get(semester);

            if (requiredSubjects == null || requiredSubjects.isEmpty()) {
                continue;
            }

            // Shuffle time slots to avoid biasing particular slots
            List<DayTimeSlot> shuffledSlots = new ArrayList<>(slots);
            Collections.shuffle(shuffledSlots);

            for (LecturerSubject subject : requiredSubjects) {
                // Try to find an available slot, room and ensure lecturer isn't double-booked
                boolean scheduled = false;

                for (DayTimeSlot slot : shuffledSlots) {
                    if (slot == null || slot.getDays() == null) continue;

                    String timeKey = slot.getDays().getDayId() + "-" + slot.getStartTime();
                    String lecturerKey = subject.getLecturer().getId() + "-" + timeKey;
                    String semesterKey = semester.getProgramSemesterId() + "-" + timeKey;

                    // Skip if semester already has a class at this time
                    if (semesterBookings.containsKey(semesterKey)) {
                        continue;
                    }

                    // Skip if lecturer already busy at this time
                    if (lecturerBookings.containsKey(lecturerKey)) {
                        continue;
                    }

                    // Find an available room
                    Room selectedRoom = null;
                    for (Room room : rooms) {
                        String roomKey = room.getRoomId() + "-" + timeKey;
                        if (!roomBookings.containsKey(roomKey)) {
                            selectedRoom = room;
                            roomBookings.put(roomKey, true);
                            break;
                        }
                    }

                    if (selectedRoom != null) {
                        // Create timetable entry
                        TimeTable entry = new TimeTable();
                        entry.setProgramSemester(semester);
                        entry.setLecturerSubject(subject);
                        entry.setDayTimeSlot(slot);
                        entry.setRoom(selectedRoom);
                        entry.setStartTime(LocalTime.parse(slot.getStartTime()));
                        entry.setEndTime(LocalTime.parse(slot.getEndTime()));

                        timetable.add(entry);

                        // Mark resources as used
                        lecturerBookings.put(lecturerKey, true);
                        semesterBookings.put(semesterKey, true);

                        scheduled = true;
                        break;
                    }
                }

                if (!scheduled) {
                    // Log warning if a subject couldn't be scheduled
                    System.out.println("⚠️ Could not schedule " + subject.getSubject().getSubjectName() +
                            " for " + semester.getTitle());
                }
            }
        }

        return timetable;
    }

    public List<List<TimeTable>> getPopulation() {
        return population;
    }

    public List<TimeTable> getIndividual(int index) {
        if (index >= 0 && index < population.size()) {
            return population.get(index);
        }
        return null;
    }

    public void updateIndividual(int index, List<TimeTable> newTimetable) {
        if (index >= 0 && index < population.size()) {
            population.set(index, newTimetable);
        }
    }
}
