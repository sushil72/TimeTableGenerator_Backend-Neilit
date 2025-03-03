package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.Days;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DayRepository extends MongoRepository<Days,String> {

}
