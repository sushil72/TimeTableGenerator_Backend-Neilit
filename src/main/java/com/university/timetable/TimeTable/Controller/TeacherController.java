package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Teacher;
import com.university.timetable.TimeTable.Services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController{
    @Autowired
    TeacherService teacherService;
    @GetMapping("/hello")
    public String teacher() {
        return "Greetings from teacher";
    }

    @GetMapping("/allTeachers")
    public ResponseEntity<List<Teacher>> allTeachers() {
        return teacherService.getAllTeachers();
    }

    @PostMapping("/addTeacher")
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

}
