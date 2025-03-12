package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Subject;
import com.university.timetable.TimeTable.Repository.SubjectRepository;
import com.university.timetable.TimeTable.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository SubjectRepository;
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(SubjectRepository.findAll());
    }

    public ResponseEntity<Subject> addSubject(Subject Subject) {
        Optional<Subject> dbSubject = SubjectRepository.findById(String.valueOf(Subject.getId()));
        if(dbSubject.isPresent()) {
            throw new IllegalStateException("Subject already exists");
        }
        Subject newSubject = new Subject();
        newSubject.setId(Subject.getId());
        newSubject.setSubjectName(Subject.getSubjectName());
        newSubject.setCrsHrs(Subject.getCrsHrs());
        newSubject.setCode(Subject.getCode());
        newSubject.setActive(Subject.isActive());
        return ResponseEntity.ok(SubjectRepository.save(newSubject));
    }

    public ResponseEntity<List<Subject>> addMultiple(List<Subject> subjects) {
        return new ResponseEntity<>(SubjectRepository.saveAll(subjects), HttpStatus.OK);
    }
}
