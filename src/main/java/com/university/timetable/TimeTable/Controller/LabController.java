package com.university.timetable.TimeTable.Controller;
import com.university.timetable.TimeTable.Entity.Lab;
import com.university.timetable.TimeTable.Services.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lab")
public class LabController {
    @Autowired
    private LabService labService;
    @GetMapping(".allLabs")
    public ResponseEntity<List<Lab>> getAllLabs() {
        return labService.getAllLabs();
    }

    @PostMapping("/addLab")
    private ResponseEntity<Lab> addLab(@RequestBody Lab lab) {
        return labService.addLab(lab);
    }
}
