package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.TimeTableRepository;
import com.university.timetable.TimeTable.Services.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Slf4j
public class TimeTableController {

    private final TimeTableService timeTableService;
    private final TimeTableRepository timeTableRepository;

    /**
     * Endpoint to generate optimal timetable
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateTimetable(
            @RequestParam(value = "clearExisting", defaultValue = "true") boolean clearExisting) {

        try {
            log.info("Received request to generate timetable with clearExisting={}", clearExisting);
            List<TimeTable> timetable = timeTableService.generateOptimalTimetable(clearExisting);

            // Validate the generated timetable
            List<String> violations = timeTableService.validateTimetable(timetable);

            if (!violations.isEmpty()) {
                log.warn("Generated timetable has {} constraint violations", violations.size());
                return ResponseEntity.ok(Map.of(
                        "status", "warning",
                        "message", "Timetable generated with some constraint violations",
                        "timetable", timetable,
                        "violations", violations
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Optimal timetable generated successfully",
                    "count", timetable.size(),
                    "timetable", timetable
            ));
        } catch (Exception e) {
            log.error("Failed to generate timetable: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to generate timetable: " + e.getMessage()
            ));
        }
    }

    /**
     * Endpoint to get current timetable
     */
    @GetMapping
    public ResponseEntity<?> getTimetable() {
        try {
            List<TimeTable> timetable = timeTableRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "count", timetable.size(),
                    "timetable", timetable
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve timetable: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to retrieve timetable: " + e.getMessage()
            ));
        }
    }
}