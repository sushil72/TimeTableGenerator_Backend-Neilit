package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Crossover {
    private final Random random = new Random();

    public List<TimeTable> crossover(List<TimeTable> parent1, List<TimeTable> parent2, List<List<TimeTable>> existingSchedules) {
        if (parent1 == null || parent2 == null || parent1.isEmpty() || parent2.isEmpty()) {
            throw new IllegalArgumentException("Crossover failed: One or both parents are null or empty!");
        }

        // Create a new timetable by combining entries from both parents
        List<TimeTable> child = new ArrayList<>();

        // Maps to track resource usage to prevent double booking
        Map<String, Boolean> roomBookings = new HashMap<>();
        Map<String, Boolean> lecturerBookings = new HashMap<>();
        Map<String, Boolean> semesterBookings = new HashMap<>();

        // Flatten the existing schedules for easier processing
        List<TimeTable> allEntries = existingSchedules.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Create a set of program semesters that need to be scheduled
        Set<ProgramSemester> semestersToSchedule = new HashSet<>();

        // Add all semesters from both parents
        parent1.forEach(entry -> {
            if (entry.getProgramSemester() != null) {
                semestersToSchedule.add(entry.getProgramSemester());
            }
        });

        parent2.forEach(entry -> {
            if (entry.getProgramSemester() != null) {
                semestersToSchedule.add(entry.getProgramSemester());
            }
        });

        // For each semester, create entries by combining from both parents
        for (ProgramSemester semester : semestersToSchedule) {
            // Get all entries for this semester from both parents
            List<TimeTable> parent1Entries = parent1.stream()
                    .filter(e -> e.getProgramSemester() != null && e.getProgramSemester().equals(semester))
                    .collect(Collectors.toList());

            List<TimeTable> parent2Entries = parent2.stream()
                    .filter(e -> e.getProgramSemester() != null && e.getProgramSemester().equals(semester))
                    .collect(Collectors.toList());

            // Create a set of all lecturer-subject combinations that need to be scheduled
            Set<LecturerSubject> subjectsToSchedule = new HashSet<>();

            parent1Entries.forEach(entry -> {
                if (entry.getLecturerSubject() != null) {
                    subjectsToSchedule.add(entry.getLecturerSubject());
                }
            });

            parent2Entries.forEach(entry -> {
                if (entry.getLecturerSubject() != null) {
                    subjectsToSchedule.add(entry.getLecturerSubject());
                }
            });

            // For each subject, create a new entry by crossover
            for (LecturerSubject subject : subjectsToSchedule) {
                TimeTable newEntry = new TimeTable();
                newEntry.setProgramSemester(semester);
                newEntry.setLecturerSubject(subject);

                // Randomly choose time slot from either parent or find a free one
                TimeTable sourceEntry;

                // Find matching entries from both parents
                Optional<TimeTable> p1Match = parent1Entries.stream()
                        .filter(e -> e.getLecturerSubject() != null && e.getLecturerSubject().equals(subject))
                        .findFirst();

                Optional<TimeTable> p2Match = parent2Entries.stream()
                        .filter(e -> e.getLecturerSubject() != null && e.getLecturerSubject().equals(subject))
                        .findFirst();

                // Choose randomly between parents if both have this subject
                if (p1Match.isPresent() && p2Match.isPresent()) {
                    sourceEntry = random.nextBoolean() ? p1Match.get() : p2Match.get();
                } else if (p1Match.isPresent()) {
                    sourceEntry = p1Match.get();
                } else if (p2Match.isPresent()) {
                    sourceEntry = p2Match.get();
                } else {
                    // Should not happen if we constructed subjectsToSchedule correctly
                    continue;
                }

                // Assign time slot
                if (random.nextDouble() > 0.3) { // 70% chance to inherit parent's slot
                    newEntry.setDayTimeSlot(sourceEntry.getDayTimeSlot());
                } else {
                    // Find a non-conflicting time slot
                    List<DayTimeSlot> allUsedSlots = allEntries.stream()
                            .filter(e -> e.getDayTimeSlot() != null)
                            .map(TimeTable::getDayTimeSlot)
                            .distinct()
                            .collect(Collectors.toList());

                    // Try to find a slot without conflicts
                    for (DayTimeSlot slot : allUsedSlots) {
                        if (slot == null) continue;

                        String timeKey = slot.getDays().getDayId() + "-" + slot.getStartTime();
                        String lecturerKey = subject.getLecturer().getId() + "-" + timeKey;
                        String semesterKey = semester.getProgramSemesterId() + "-" + timeKey;

                        // Skip if there are conflicts
                        if (lecturerBookings.containsKey(lecturerKey) || semesterBookings.containsKey(semesterKey)) {
                            continue;
                        }

                        newEntry.setDayTimeSlot(slot);
                        break;
                    }

                    // If no slot found, use parent's slot
                    if (newEntry.getDayTimeSlot() == null) {
                        newEntry.setDayTimeSlot(sourceEntry.getDayTimeSlot());
                    }
                }

                // If we have a day time slot, mark the resources as used
                if (newEntry.getDayTimeSlot() != null && newEntry.getLecturerSubject() != null) {
                    String timeKey = newEntry.getDayTimeSlot().getDays().getDayId() + "-" +
                            newEntry.getDayTimeSlot().getStartTime();
                    String lecturerKey = newEntry.getLecturerSubject().getLecturer().getId() + "-" + timeKey;
                    String semesterKey = semester.getProgramSemesterId() + "-" + timeKey;

                    lecturerBookings.put(lecturerKey, true);
                    semesterBookings.put(semesterKey, true);
                }

                // Assign room based on subject type
                if (newEntry.getLecturerSubject() != null && newEntry.getLecturerSubject().getSubject() != null) {
                    String subjectName = newEntry.getLecturerSubject().getSubject().getSubjectName().toLowerCase();

                    // Find available rooms from parents
                    List<Room> availableRooms = new ArrayList<>();
                    if (p1Match.isPresent() && p1Match.get().getRoom() != null) {
                        availableRooms.add(p1Match.get().getRoom());
                    }
                    if (p2Match.isPresent() && p2Match.get().getRoom() != null) {
                        availableRooms.add(p2Match.get().getRoom());
                    }

                    // Add additional rooms from other entries
                    allEntries.stream()
                            .filter(e -> e.getRoom() != null)
                            .forEach(e -> availableRooms.add(e.getRoom()));

                    // Filter for appropriate room type
                    List<Room> appropriateRooms;
                    if (subjectName.contains("lab") || subjectName.contains("practical")) {
                        appropriateRooms = availableRooms.stream()
                                .filter(room -> room.getRoomType() != null &&
                                        room.getRoomType().getName().toLowerCase().contains("lab"))
                                .collect(Collectors.toList());
                    } else {
                        appropriateRooms = availableRooms.stream()
                                .filter(room -> room.getRoomType() != null &&
                                        (room.getRoomType().getName().toLowerCase().contains("lecture") ||
                                                room.getRoomType().getName().toLowerCase().contains("classroom")))
                                .collect(Collectors.toList());
                    }

                    // Choose a non-conflicting room
                    if (!appropriateRooms.isEmpty()) {
                        Collections.shuffle(appropriateRooms);

                        for (Room room : appropriateRooms) {
                            if (newEntry.getDayTimeSlot() != null) {
                                String timeKey = newEntry.getDayTimeSlot().getDays().getDayId() + "-" +
                                        newEntry.getDayTimeSlot().getStartTime();
                                String roomKey = room.getRoomId() + "-" + timeKey;

                                if (!roomBookings.containsKey(roomKey)) {
                                    newEntry.setRoom(room);
                                    roomBookings.put(roomKey, true);
                                    break;
                                }
                            }
                        }
                    }

                    // If no appropriate room found, use parent's room
                    if (newEntry.getRoom() == null && sourceEntry.getRoom() != null) {
                        newEntry.setRoom(sourceEntry.getRoom());
                    }
                }

                // Set start and end times based on day time slot
                if (newEntry.getDayTimeSlot() != null) {
                    if (newEntry.getDayTimeSlot().getStartTime() != null) {
                        newEntry.setStartTime(LocalTime.parse(newEntry.getDayTimeSlot().getStartTime()));
                    }
                    if (newEntry.getDayTimeSlot().getEndTime() != null) {
                        newEntry.setEndTime(LocalTime.parse(newEntry.getDayTimeSlot().getEndTime()));
                    }
                }

                // Add the new entry to child timetable
                child.add(newEntry);
            }
        }

        return child;
    }
}