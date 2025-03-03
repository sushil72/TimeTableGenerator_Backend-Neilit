package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.ProgramSemester;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramSemesterRepository extends MongoRepository <ProgramSemester, String> {

}
