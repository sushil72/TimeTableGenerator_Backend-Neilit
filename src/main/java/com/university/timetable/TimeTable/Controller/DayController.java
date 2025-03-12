package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Days;
import com.university.timetable.TimeTable.Services.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/day")
public class DayController {
    @Autowired
    DayService dayService;

    @GetMapping("/get-days")
    public ResponseEntity<List<Days>> getAllDays() {
        return dayService.getAllDays();
    }

    @PostMapping("/add-day")
    public ResponseEntity<Days> addDays(@RequestBody Days days) {
        return  dayService.addDay(days);
    }

    @PostMapping("/add-days")
    public ResponseEntity<List<Days>> addMultipleDays(@RequestBody List<Days> daysList) {
        return ResponseEntity.ok(dayService.addDaysList(daysList));
    }
}
