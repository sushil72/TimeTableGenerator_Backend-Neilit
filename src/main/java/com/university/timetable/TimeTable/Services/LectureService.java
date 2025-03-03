package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Lecturer;
import com.university.timetable.TimeTable.Repository.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LecturerRepository lecturerRepository;

    // Add a new lecturer
    public Lecturer addLecturer(Lecturer lecturer) {
        return lecturerRepository.save(lecturer);
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
            lecturer.setSubjects(lecturerDetails.getSubjects());
            lecturer.setAvailable(lecturerDetails.isAvailable());
            return lecturerRepository.save(lecturer);
        }).orElseThrow(() -> new RuntimeException("Lecturer not found with ID: " + id));
    }

    // Delete a lecturer
    public void deleteLecturer(String id) {
        lecturerRepository.deleteById(id);
    }
}
