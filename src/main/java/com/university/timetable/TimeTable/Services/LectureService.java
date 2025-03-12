package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Lecturer;
import com.university.timetable.TimeTable.Repository.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LectureService {
    @Autowired
    private  LecturerRepository lecturerRepository;

    // Add a new lecturer
    public ResponseEntity<Lecturer> addLecturer(Lecturer lecturer) {
        Optional<Lecturer> lect = lecturerRepository.findById(lecturer.getId());
        if (lect.isPresent()) {
            throw  new RuntimeException("Lecturer already exists");
        }
        Lecturer newLecturer = new Lecturer();
        newLecturer.setId(lecturer.getId());
        newLecturer.setEmail(lecturer.getEmail());
        newLecturer.setDepartment(lecturer.getDepartment());
        newLecturer.setAvailable(lecturer.isAvailable());
        newLecturer.setSubjectsTaught(lecturer.getSubjectsTaught());
        newLecturer.setName(lecturer.getName());
        return ResponseEntity.ok(lecturerRepository.save(lecturer));
    }

    // Retrieve all lecturers
    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    // Get a lecturer by ID
    public Optional<Lecturer> getLecturerById(String id) {
        return lecturerRepository.findById(id);
    }
    // Update lecturer details
    public Lecturer updateLecturer(String id, Lecturer lecturerDetails) {
        return lecturerRepository.findById(id).map(lecturer -> {
            lecturer.setName(lecturerDetails.getName());
            lecturer.setEmail(lecturerDetails.getEmail());
            lecturer.setDepartment(lecturerDetails.getDepartment());
            lecturer.setAvailable(lecturerDetails.isAvailable());
            lecturer.setSubjectsTaught(lecturerDetails.getSubjectsTaught());
            return lecturerRepository.save(lecturer);
        }).orElseThrow(() -> new RuntimeException("Lecturer not found with ID: " + id));
    }
    // Delete a lecturer
    public void deleteLecturer(String id) {
        lecturerRepository.deleteById(id);
    }

    public ResponseEntity<List<Lecturer>> addMultiple(List<Lecturer> lecturers) {
        return new ResponseEntity<>(lecturerRepository.saveAll(lecturers), HttpStatus.OK);
    }
}
