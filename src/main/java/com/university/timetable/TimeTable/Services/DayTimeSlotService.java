package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.DayTimeSlot;
import com.university.timetable.TimeTable.Repository.DayTimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DayTimeSlotService {
    @Autowired
    private DayTimeSlotRepository dayTimeSlotRepository;

    public ResponseEntity<List<DayTimeSlot>> getDayTimeSlot() {
        return new ResponseEntity<>(dayTimeSlotRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<DayTimeSlot> addDatSlotTime(DayTimeSlot dayTimeSlot) {
        Optional<DayTimeSlot> db =dayTimeSlotRepository.findById(dayTimeSlot.getDayId());
        if (db.isPresent()) {
            throw new RuntimeException("Day time slot already exists");
        }
        DayTimeSlot newSlot = new DayTimeSlot();
        newSlot.setDayId(dayTimeSlot.getDayId());
        newSlot.setStartTime(dayTimeSlot.getStartTime());
        newSlot.setEndTime(dayTimeSlot.getEndTime());
        newSlot.setSlotTitle(dayTimeSlot.getSlotTitle());
        newSlot.setDays(dayTimeSlot.getDays());
        newSlot.setActive(dayTimeSlot.isActive());
        DayTimeSlot saved = dayTimeSlotRepository.save(newSlot);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
