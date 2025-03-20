package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.TimeTableRepository;
import com.university.timetable.TimeTable.Repository.DayTimeSlotRepository;
import com.university.timetable.TimeTable.Repository.ProgramSemesterRepository;
import com.university.timetable.TimeTable.Repository.LecturerSubjectRepository;
import com.university.timetable.TimeTable.Repository.RoomRepository;
import com.university.timetable.TimeTable.TimeTableGeneration.CSPBacktrackingSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;
    private final CSPBacktrackingSolver solver;

    /**
     * Generates an optimal timetable using constraint satisfaction with backtracking
     *
     * @param clearExisting Whether to clear existing timetable entries before generating new ones
     * @return List of timetable entries representing the optimal solution
     */
    @Transactional
    public List<TimeTable> generateOptimalTimetable(boolean clearExisting) {
        log.info("🚀 Starting Constraint Satisfaction Backtracking Solver...");

        try {
            // Clear existing timetable if requested
            if (clearExisting) {
                log.info("🧹 Clearing existing timetable data");
                timeTableRepository.deleteAll();
            }

            // Solve using CSP Backtracking
            List<TimeTable> bestTimetable = solver.solve();

            // Verify solution validity
            if (bestTimetable.isEmpty()) {
                log.warn("⚠️ Generated timetable is empty!");
                throw new RuntimeException("Failed to generate a valid timetable!");
            }

            log.info("✅ Found valid timetable with {} entries", bestTimetable.size());

            // Save and return the best solution
            timeTableRepository.saveAll(bestTimetable);
            log.info("🎯 Best Timetable successfully generated and saved!");

            return bestTimetable;
        } catch (RuntimeException e) {
            log.error("❌ Failed to generate timetable: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Convenience method that defaults to clearing existing entries
     */
    @Transactional
    public List<TimeTable> generateOptimalTimetable() {
        return generateOptimalTimetable(true);
    }

    /**
     * Validates a generated timetable for constraint violations
     */
    public List<String> validateTimetable(List<TimeTable> timetable) {
        List<String> violations = new ArrayList<>();

        // Check for lecturer conflicts
        Map<String, List<TimeTable>> lecturerAssignments = new HashMap<>();

        for (TimeTable entry : timetable) {
            String lecturerId = entry.getLecturerSubject().getLecturer().getId();
            lecturerAssignments.computeIfAbsent(lecturerId, k -> new ArrayList<>()).add(entry);
        }

        for (Map.Entry<String, List<TimeTable>> entry : lecturerAssignments.entrySet()) {
            List<TimeTable> assignments = entry.getValue();
            for (int i = 0; i < assignments.size(); i++) {
                for (int j = i + 1; j < assignments.size(); j++) {
                    if (assignments.get(i).getDayTimeSlot().equals(assignments.get(j).getDayTimeSlot())) {
                        violations.add("Lecturer conflict: " + assignments.get(i).getLecturerSubject().getLecturer().getName() +
                                " assigned to multiple classes at " + assignments.get(i).getDayTimeSlot().getStartTime());
                    }
                }
            }
        }

        // Add more validation checks as needed

        return violations;
    }
}