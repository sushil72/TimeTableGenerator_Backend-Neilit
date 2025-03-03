package com.university.timetable.TimeTable.Repository;


import com.university.timetable.TimeTable.Entity.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends MongoRepository<Semester,String> {
}
