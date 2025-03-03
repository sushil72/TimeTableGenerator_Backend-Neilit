package com.university.timetable.TimeTable.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
public class LecturerSubject {
    private String lecturerSubjectId;
    private String Title;
    @DBRef
    Teacher lecturer;
    @DBRef
    Subject subject;
}
