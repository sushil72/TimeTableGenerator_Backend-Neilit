package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {

}
