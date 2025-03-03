package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.Lab;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends MongoRepository<Lab, String> {
}
