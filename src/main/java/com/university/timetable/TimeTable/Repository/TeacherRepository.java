package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {
}
