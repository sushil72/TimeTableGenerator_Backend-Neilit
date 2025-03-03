package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Teacher;
import com.university.timetable.TimeTable.Repository.RoomRepository;
import com.university.timetable.TimeTable.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    private RoomRepository roomRepository;

    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> lecturers = teacherRepository.findAll();
    return new ResponseEntity<>(lecturers, HttpStatus.OK);
    }

    public ResponseEntity<Teacher> addTeacher(Teacher teacher) {
        Optional<Teacher> dbTeacher = teacherRepository.findById(teacher.getId());
        if(dbTeacher.isPresent()) {
            throw new IllegalStateException("Teacher already exists");
        }

        Teacher newTeacher = new Teacher();
        newTeacher.setId(teacher.getId());
        newTeacher.setName(teacher.getName());
        newTeacher.setContactNumber(teacher.getContactNumber());
        newTeacher.setIsActive(teacher.getIsActive());
        return new ResponseEntity<>(teacherRepository.save(newTeacher), HttpStatus.CREATED);
    }

}
