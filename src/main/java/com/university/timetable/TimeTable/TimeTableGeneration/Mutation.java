package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.DayTimeSlot;
import com.university.timetable.TimeTable.Entity.Room;
import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.DayTimeSlotRepository;
import com.university.timetable.TimeTable.Repository.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class Mutation {
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final RoomRepository roomRepository;
    private final Random random = new Random();

    public Mutation(DayTimeSlotRepository dayTimeSlotRepository, RoomRepository roomRepository) {
        this.dayTimeSlotRepository = dayTimeSlotRepository;
        this.roomRepository = roomRepository;
    }

    public void mutate(List<TimeTable> timetable, List<List<TimeTable>> allSchedules) {
        // Apply mutation to each entry in the timetable
        for (TimeTable entry : timetable) {
            mutateEntry(entry, flattenAllSchedules(allSchedules));
        }
    }

    private void mutateEntry(TimeTable entry, List<TimeTable> allEntries) {
        if (entry == null || entry.getLecturerSubject() == null ||
                entry.getLecturerSubject().getSubject() == null) {
            return; // Prevent NPE
        }

        // 10% chance to change room
        if (random.nextDouble() < 0.1) {
            List<Room> compatibleRooms = roomRepository.findAll().stream()
                    .filter(room -> room.getRoomType() != null &&
                            entry.getLecturerSubject() != null &&
                            entry.getLecturerSubject().getSubject() != null &&
                            isRoomCompatible(room, entry))
                    .collect(Collectors.toList());

            if (!compatibleRooms.isEmpty()) {
                entry.setRoom(compatibleRooms.get(random.nextInt(compatibleRooms.size())));
            }
        }

        // 10% chance to change timeslot
        if (random.nextDouble() < 0.1) {
            List<DayTimeSlot> slots = dayTimeSlotRepository.findAll();
            if (!slots.isEmpty()) {
                DayTimeSlot newSlot;
                int attempts = 0;
                do {
                    newSlot = slots.get(random.nextInt(slots.size()));
                    attempts++;
                    // Limit attempts to avoid infinite loop
                } while (newSlot != null && isLecturerBusy(entry, newSlot, allEntries) && attempts < 10);

                if (attempts < 10) {
                    entry.setDayTimeSlot(newSlot);
                    // Update start and end times based on the slot
                    if (newSlot.getStartTime() != null) {
                        entry.setStartTime(java.time.LocalTime.parse(newSlot.getStartTime()));
                    }
                    if (newSlot.getEndTime() != null) {
                        entry.setEndTime(java.time.LocalTime.parse(newSlot.getEndTime()));
                    }
                }
            }
        }
    }

    // Helper to check if room is compatible with the subject
    private boolean isRoomCompatible(Room room, TimeTable entry) {
        if (entry.getLecturerSubject() == null || entry.getLecturerSubject().getSubject() == null) {
            return false;
        }

        String subjectName = entry.getLecturerSubject().getSubject().getSubjectName().toLowerCase();
        String roomTypeName = room.getRoomType() != null ? room.getRoomType().getName().toLowerCase() : "";

        // Check if lab subjects are assigned to lab rooms
        if (subjectName.contains("lab") || subjectName.contains("practical")) {
            return roomTypeName.contains("lab");
        } else {
            // For regular subjects, prefer lecture halls
            return roomTypeName.contains("lecture") || roomTypeName.contains("classroom");
        }
    }

    // Check if lecturer is already scheduled at this time
    private boolean isLecturerBusy(TimeTable entry, DayTimeSlot newSlot, List<TimeTable> allEntries) {
        if (entry.getLecturerSubject() == null || entry.getLecturerSubject().getLecturer() == null) {
            return false;
        }

        return allEntries.stream()
                .filter(tt -> tt != null && tt != entry && tt.getDayTimeSlot() != null &&
                        tt.getLecturerSubject() != null && tt.getLecturerSubject().getLecturer() != null)
                .anyMatch(tt -> tt.getDayTimeSlot().equals(newSlot) &&
                        tt.getLecturerSubject().getLecturer().equals(entry.getLecturerSubject().getLecturer()));
    }

    // Flatten the nested list structure for easier processing
    private List<TimeTable> flattenAllSchedules(List<List<TimeTable>> allSchedules) {
        return allSchedules.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}