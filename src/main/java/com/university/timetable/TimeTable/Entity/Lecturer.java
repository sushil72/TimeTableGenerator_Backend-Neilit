package com.university.timetable.TimeTable.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "lecturer")
public class Lecturer {
    @Id
    private String id;
    private String name;
    private String email;
    private String department;
    private boolean isAvailable;
    private List<String> subjectsTaught;
    // ✅ Store subject IDs

}
