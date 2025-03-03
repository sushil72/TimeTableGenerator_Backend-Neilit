package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.Lab;
import com.university.timetable.TimeTable.Repository.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabService {
    @Autowired
    LabRepository labRepository;
    public ResponseEntity<List<Lab>> getAllLabs() {
        return  ResponseEntity.ok(labRepository.findAll());
    }

    public ResponseEntity<Lab> addLab(Lab lab) {
    Optional<Lab> Dblab = labRepository.findById(lab.getLabId());
    if(Dblab.isPresent()) {
        throw new IllegalStateException("Lab already exists");
    }
    Lab newLab = new Lab();
    newLab.setLabId(lab.getLabId());
    newLab.setLabName(lab.getLabName());
    newLab.setLabNumber(lab.getLabNumber());
    newLab.setActive(lab.isActive());
    newLab.setCapacity(lab.getCapacity());
    return new ResponseEntity<>(labRepository.save(lab), HttpStatus.CREATED);
    }
}
