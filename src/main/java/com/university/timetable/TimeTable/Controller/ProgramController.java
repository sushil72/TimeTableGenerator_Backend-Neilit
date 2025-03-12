package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.Program;
import com.university.timetable.TimeTable.Entity.ProgramSemester;
import com.university.timetable.TimeTable.Entity.Semester;
import com.university.timetable.TimeTable.Services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/program")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @PostMapping("/add")
    public ResponseEntity<Program> addProgram(@RequestBody Program program) {
        return ResponseEntity.ok(programService.addProgram(program));
    }

    @PostMapping("/semester")
    public ResponseEntity<ProgramSemester> addSemester(@RequestBody ProgramSemester semester) {
        return programService.addSemester(semester);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Program>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable String id) {
        return ResponseEntity.ok(programService.getProgramById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProgram(@PathVariable String id) {
        programService.deleteProgram(id);
        return ResponseEntity.ok("Program deleted successfully");
    }
}
