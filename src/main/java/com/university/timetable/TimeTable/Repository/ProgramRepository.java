package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.Program;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends MongoRepository<Program, String> {
}
