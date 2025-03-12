package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Lecturer;
import com.university.timetable.TimeTable.Services.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("api/lecturer")
@RequiredArgsConstructor
public class LecturerController {
    private final LectureService lecturerService;

    // Add a new lecturer
    @PostMapping("/add")
    public ResponseEntity<Lecturer> addLecturer(@RequestBody Lecturer lecturer) {
        return lecturerService.addLecturer(lecturer);
    }

    // Retrieve all lecturers
    @GetMapping("/all")
    public ResponseEntity<List<Lecturer>> getAllLecturers() {
        return ResponseEntity.ok(lecturerService.getAllLecturers());
    }

    // Get a specific lecturer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Lecturer>> getLecturerById(@PathVariable String id) {
        return ResponseEntity.ok(lecturerService.getLecturerById(id));
    }

    // Update lecturer details
    @PutMapping("/update/{id}")
    public ResponseEntity<Lecturer> updateLecturer(@PathVariable String id, @RequestBody Lecturer lecturerDetails) {
        return ResponseEntity.ok(lecturerService.updateLecturer(id, lecturerDetails));
    }

    // Delete a lecturer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLecturer(@PathVariable String id) {
        lecturerService.deleteLecturer(id);
        return ResponseEntity.ok("Lecturer with ID " + id + " deleted successfully.");
    }
    @PostMapping("/add-lectures")
    public ResponseEntity<List<Lecturer>> addLecturers(@RequestBody List<Lecturer> lecturers) {
        return lecturerService.addMultiple(lecturers);
    }
}
