package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Subject;
import com.university.timetable.TimeTable.Services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/subject")
@PreAuthorize("hasAuthority('ADMIN')")
public class SubjectController {
   @Autowired
    private SubjectService subjectService;

   @GetMapping("/get-subjects")
   public ResponseEntity<List<Subject>> getAllCourse()
   {
       return subjectService.getAllSubjects();
   }

   @PostMapping("/add-subject")
    public ResponseEntity<Subject> addCourse(@RequestBody  Subject subject)
   {
        log.debug("Subject ",subject);
       return subjectService.addSubject(subject);
   }

   @PostMapping("/add-subjects")
    public ResponseEntity<List<Subject>> addMultipleSubjects(@RequestBody  List<Subject> subjects){
       return subjectService.addMultiple(subjects);
   }

 }
