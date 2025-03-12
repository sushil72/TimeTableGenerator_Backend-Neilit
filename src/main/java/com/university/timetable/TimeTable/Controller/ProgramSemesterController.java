package com.university.timetable.TimeTable.Controller;

import com.university.timetable.TimeTable.Entity.ProgramSemester;
import com.university.timetable.TimeTable.Services.ProgramSemesterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programSemester")
public class ProgramSemesterController {

    private final ProgramSemesterService programSemesterService;

    public ProgramSemesterController(ProgramSemesterService programSemesterService) {
        this.programSemesterService = programSemesterService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProgramSemester> addProgramSemester(@RequestBody ProgramSemester programSemester) {
        return ResponseEntity.ok(programSemesterService.addProgramSemester(programSemester));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProgramSemester>> getAllProgramSemesters() {
        return ResponseEntity.ok(programSemesterService.getAllProgramSemesters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramSemester> getProgramSemesterById(@PathVariable String id) {
        return ResponseEntity.ok(programSemesterService.getProgramSemesterById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgramSemester(@PathVariable String id) {
        programSemesterService.deleteProgramSemester(id);
        return ResponseEntity.ok("Program Semester deleted successfully.");
    }
    @PostMapping("/add-programsemester")
    public ResponseEntity<List<ProgramSemester>> addProgramSemesters(@RequestBody List<ProgramSemester> programSemesters) {
        return programSemesterService.addMore(programSemesters);
    }
}
