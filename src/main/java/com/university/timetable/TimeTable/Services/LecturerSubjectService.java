package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.LecturerSubject;
import com.university.timetable.TimeTable.Repository.LecturerSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LecturerSubjectService {
    private final LecturerSubjectRepository lecSubRepo;
    private final LecturerSubjectRepository lecturerSubjectRepository;

    // Add a new LecturerSubject mapping
    public LecturerSubject addLecturerSubject(LecturerSubject lecturerSubject) {
        return lecturerSubjectRepository.save(lecturerSubject);
    }

    // Retrieve all LecturerSubject mappings
    public List<LecturerSubject> getAllLecturerSubjects() {
        return lecturerSubjectRepository.findAll();
    }

    // Retrieve a specific LecturerSubject by ID
    public Optional<LecturerSubject> getLecturerSubjectById(String id) {
        return lecturerSubjectRepository.findById(id);
    }

    // Update an existing LecturerSubject
    public LecturerSubject updateLecturerSubject(String id, LecturerSubject lecturerSubjectDetails) {
        return lecturerSubjectRepository.findById(id).map(lecturerSubject -> {
            lecturerSubject.setLecturer(lecturerSubjectDetails.getLecturer());
            lecturerSubject.setSubject(lecturerSubjectDetails.getSubject());
            return lecturerSubjectRepository.save(lecturerSubject);
        }).orElseThrow(() -> new RuntimeException("LecturerSubject not found with ID: " + id));
    }

    // Delete a LecturerSubject mapping
    public void deleteLecturerSubject(String id) {
        lecturerSubjectRepository.deleteById(id);
    }
}
