package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.Lecturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository extends MongoRepository<Lecturer, String> {
}
