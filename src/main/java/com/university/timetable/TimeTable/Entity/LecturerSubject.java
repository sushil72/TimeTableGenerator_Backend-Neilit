package com.university.timetable.TimeTable.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class LecturerSubject {
    @Id
    private String lecturerSubjectId;
    private String title;
    @DBRef
    Lecturer lecturer;
    @DBRef
    Subject subject;
}
