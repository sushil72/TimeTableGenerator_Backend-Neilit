package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Services.TimeTableService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/time-table")
public class TimeTableController {
    private final TimeTableService timeTableService;

    public TimeTableController(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    @GetMapping("/generate")
    public ResponseEntity<TimeTable> generateOptimalTimetable() {
        TimeTable bestTimetable = timeTableService.runGeneticAlgorithm();
        return ResponseEntity.ok(bestTimetable);
    }
}