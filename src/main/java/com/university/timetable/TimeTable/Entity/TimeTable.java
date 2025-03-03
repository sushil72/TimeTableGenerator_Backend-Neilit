package com.university.timetable.TimeTable.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="timetables")
public class TimeTable {
    @Id
    private String timeTableId;
    @DBRef
    DayTimeSlot dayTimeSlot;
    @DBRef
    private ProgramSemester programSemester;
    @DBRef
    private LecturerSubject lecturerSubject;
    @DBRef
    private Room room;
    private  LocalTime startTime;
    private LocalTime endTime;
}
