package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class ProgramSemester {
    @Id
    private String programSemesterId;

    private String title; // Fixed naming convention

    @DBRef
    private Program program; // Reference to a specific program (e.g., MCA, B.Tech)

    @DBRef
    private Semester semester;

    private int totalStudents;
}
