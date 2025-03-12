package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Days;
import com.university.timetable.TimeTable.Repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DayService {
    @Autowired
    DayRepository dayRepository;
    public ResponseEntity<List<Days>> getAllDays() {
        return ResponseEntity.ok(dayRepository.findAll());
    }

    public ResponseEntity<Days> addDay(Days day) {
        Optional<Days> dbDay = dayRepository.findById(day.getDayId());
        if(dbDay.isPresent()) {
            throw new IllegalStateException("Day already exists");
        }
        Days newDay = new Days();
        newDay.setDayName(day.getDayName());
        newDay.setDayId(day.getDayId());
        newDay.setActive(day.isActive());
        return ResponseEntity.ok(dayRepository.save(newDay));
    }

    public List<Days> addDaysList(List<Days> daysList) {
      return   dayRepository.saveAll(daysList);
    }
}