package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.DayTimeSlot;
import com.university.timetable.TimeTable.Services.DayTimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slot")
public class DaySlotController{
    @Autowired
    DayTimeSlotService dayTimeSlotService;
    @GetMapping("/get-slot")
    public String daySlot() {
        return "Day Slot";
    }

    @PostMapping("/add-slot")
    public ResponseEntity<DayTimeSlot> addSlot(@RequestBody DayTimeSlot dayTimeSlot) {
            return this.dayTimeSlotService.addDatSlotTime(dayTimeSlot);
    }

    @PostMapping("/add-all-slots")
    public ResponseEntity<List<DayTimeSlot>> addMultipleDayTimeSlots(@RequestBody List<DayTimeSlot> dayTimeSlots) {
        return ResponseEntity.ok(dayTimeSlotService.addAllTimeSlots(dayTimeSlots));
    }

}
