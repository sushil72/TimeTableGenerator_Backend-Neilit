package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Semester;
import com.university.timetable.TimeTable.Repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemesterService {
    @Autowired
    SemesterRepository semesterRepository;
    public ResponseEntity<List<Semester>> getAllService() {
        List<Semester> semesters = semesterRepository.findAll();
        return ResponseEntity.ok(semesters);
    }

    public ResponseEntity<Semester> addSemester(Semester semester) {
        Optional<Semester> Dbsemester = semesterRepository.findById(semester.getSemesterId());
        if(Dbsemester.isPresent()) {
            throw new IllegalStateException("Semester already exists");
        }
        Semester newSemester = new Semester();
        newSemester.setSemesterId(semester.getSemesterId());
        newSemester.setSemesterName(semester.getSemesterName());
        newSemester.setActive(semester.isActive());

        return ResponseEntity.ok(semesterRepository.save(newSemester));
    }
}
