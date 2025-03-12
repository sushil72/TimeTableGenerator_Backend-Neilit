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

    private String title;

    @DBRef
    private Program program;

    @DBRef
    private Semester semester;

    private int totalStudents;
}
