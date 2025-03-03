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
    @GetMapping("/getAllDays")
    public ResponseEntity<List<Days>> getAllDays() {
        return dayService.getAllDays();
    }

    @PostMapping("/addDays")
    public ResponseEntity<Days> addDays(@RequestBody Days days) {
        return  dayService.addDay(days);
    }
}
