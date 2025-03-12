package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.ProgramSemester;
import com.university.timetable.TimeTable.Repository.ProgramSemesterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramSemesterService {

    private final ProgramSemesterRepository programSemesterRepository;

    public ProgramSemesterService(ProgramSemesterRepository programSemesterRepository) {
        this.programSemesterRepository = programSemesterRepository;
    }

    public ProgramSemester addProgramSemester(ProgramSemester programSemester) {
        return programSemesterRepository.save(programSemester);
    }

    public List<ProgramSemester> getAllProgramSemesters() {
        return programSemesterRepository.findAll();
    }

    public ProgramSemester getProgramSemesterById(String id) {
        return programSemesterRepository.findById(id).orElseThrow(() -> new RuntimeException("Program Semester not found"));
    }

    public void deleteProgramSemester(String id) {
        programSemesterRepository.deleteById(id);
    }

    public ResponseEntity<List<ProgramSemester>> addMore(List<ProgramSemester> programSemesters) {
return new ResponseEntity<>(programSemesterRepository.saveAll(programSemesters), HttpStatus.OK);
    }
}
