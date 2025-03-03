package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.LecturerSubject;
import com.university.timetable.TimeTable.Services.LecturerSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecturer-subject")
@RequiredArgsConstructor
public class LecturerSubjectController {
    private final LecturerSubjectService lecturerSubjectService;

    // Add a new LecturerSubject mapping
    @PostMapping("/add")
    public ResponseEntity<LecturerSubject> addLecturerSubject(@RequestBody LecturerSubject lecturerSubject) {
        return ResponseEntity.ok(lecturerSubjectService.addLecturerSubject(lecturerSubject));
    }

    // Retrieve all LecturerSubject mappings
    @GetMapping("/all")
    public ResponseEntity<List<LecturerSubject>> getAllLecturerSubjects() {
        return ResponseEntity.ok(lecturerSubjectService.getAllLecturerSubjects());
    }

    // Get a specific LecturerSubject by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<LecturerSubject>> getLecturerSubjectById(@PathVariable String id) {
        return ResponseEntity.ok(lecturerSubjectService.getLecturerSubjectById(id));
    }

    // Update LecturerSubject mapping
    @PutMapping("/update/{id}")
    public ResponseEntity<LecturerSubject> updateLecturerSubject(@PathVariable String id, @RequestBody LecturerSubject lecturerSubjectDetails) {
        return ResponseEntity.ok(lecturerSubjectService.updateLecturerSubject(id, lecturerSubjectDetails));
    }

    // Delete a LecturerSubject mapping
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLecturerSubject(@PathVariable String id) {
        lecturerSubjectService.deleteLecturerSubject(id);
        return ResponseEntity.ok("LecturerSubject with ID " + id + " deleted successfully.");
    }
}
