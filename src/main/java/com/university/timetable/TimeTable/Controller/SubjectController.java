package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Subject;
import com.university.timetable.TimeTable.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@PreAuthorize("hasAuthority('ADMIN')")
public class SubjectController {
   @Autowired
    private SubjectService subjectService;

   @GetMapping("/getAllCouses")
   public ResponseEntity<List<Subject>> getAllCourse()
   {
       return subjectService.getAllSubjects();
   }

   @PostMapping("/addCourse")
    public ResponseEntity<Subject> addCourse(@RequestBody  Subject subject)
   {
       System.out.println("courses =: "+subject);
       return subjectService.addSubject(subject);
   }
 }
