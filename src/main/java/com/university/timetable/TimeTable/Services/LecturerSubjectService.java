package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Lecturer;
import com.university.timetable.TimeTable.Entity.LecturerSubject;
import com.university.timetable.TimeTable.Entity.Subject;
import com.university.timetable.TimeTable.Repository.LecturerRepository;
import com.university.timetable.TimeTable.Repository.LecturerSubjectRepository;
import com.university.timetable.TimeTable.Repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LecturerSubjectService {

    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final LecturerRepository lecturerRepository;
    private final SubjectRepository subjectRepository;
    // Add a new LecturerSubject mapping
    public ResponseEntity<LecturerSubject> addLecturerSubject(LecturerSubject lecturerSubject) {
        Optional<LecturerSubject> db=lecturerSubjectRepository.findById(lecturerSubject.getLecturerSubjectId());
        if(db.isPresent()) {
            throw new IllegalArgumentException("LecturerSubject already exists");
        }
        LecturerSubject newLecturerSubject = new LecturerSubject();
        newLecturerSubject.setLecturerSubjectId(lecturerSubject.getLecturerSubjectId());
        newLecturerSubject.setLecturer(lecturerSubject.getLecturer());
        newLecturerSubject.setSubject(lecturerSubject.getSubject());
        newLecturerSubject.setTitle(lecturerSubject.getTitle());
        return ResponseEntity.ok(lecturerSubjectRepository.save(newLecturerSubject));
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

    // ✅ Auto-generate Lecturer-Subject mappings
    public List<LecturerSubject> assignLecturersToSubjects() {
        List<Lecturer> lecturers = lecturerRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        List<LecturerSubject> assignedLecturers = new ArrayList<>();

        for (Subject subject : subjects) {
            // Find a lecturer who can teach this subject and has NOT been assigned yet
            Optional<Lecturer> lecturerOpt = lecturers.stream()
                    .filter(lecturer -> lecturer.getSubjectsTaught().contains(subject.getId()))
                    .findFirst(); // ✅ Ensures only ONE lecturer per subject

            if (lecturerOpt.isPresent()) {
                Lecturer lecturer = lecturerOpt.get();
                LecturerSubject lecturerSubject = new LecturerSubject(
                        UUID.randomUUID().toString(),
                        lecturer.getName() + " - " + subject.getSubjectName(),
                        lecturer,
                        subject
                );
                assignedLecturers.add(lecturerSubject);
            }
        }
        return lecturerSubjectRepository.saveAll(assignedLecturers);
    }
}
