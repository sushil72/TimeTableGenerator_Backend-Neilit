package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableRepository extends MongoRepository<TimeTable, String> {
}
