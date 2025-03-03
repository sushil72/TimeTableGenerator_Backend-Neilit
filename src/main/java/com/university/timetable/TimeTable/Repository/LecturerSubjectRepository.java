package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.LecturerSubject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LecturerSubjectRepository extends MongoRepository<LecturerSubject, String> {
}
