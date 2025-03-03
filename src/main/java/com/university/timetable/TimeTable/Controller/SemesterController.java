package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Semester;
import com.university.timetable.TimeTable.Services.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;
    @GetMapping( "/allSemesters")
    public ResponseEntity<List<Semester>> getAllSemesters() {
        return semesterService.getAllService();
    }

    @PostMapping( "/addSemester")
    private ResponseEntity<Semester> addSemester(@RequestBody Semester semester) {
        return semesterService.addSemester(semester);
    }
}
