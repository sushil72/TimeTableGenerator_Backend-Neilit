package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Program;
import com.university.timetable.TimeTable.Entity.ProgramSemester;
import com.university.timetable.TimeTable.Repository.ProgramRepository;
import com.university.timetable.TimeTable.Repository.ProgramSemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramSemesterRepository programSemesterRepository;


    public Program addProgram(Program program) {
        return programRepository.save(program);
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    public Program getProgramById(String id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
    }

    public void deleteProgram(String id) {
        programRepository.deleteById(id);
    }

    public ResponseEntity<ProgramSemester> addSemester(ProgramSemester semester) {
        Optional<ProgramSemester> DbprogramSemester = programSemesterRepository.findById(semester.getProgramSemesterId());
        if (DbprogramSemester.isPresent()) {
            throw  new RuntimeException("Semester already exists");
        }
        ProgramSemester newProgramSemester = new ProgramSemester();
        newProgramSemester.setProgramSemesterId(semester.getProgramSemesterId());
        newProgramSemester.setTitle(semester.getTitle());
        programSemesterRepository.save(semester);
        return ResponseEntity.ok(newProgramSemester);
    }
}
