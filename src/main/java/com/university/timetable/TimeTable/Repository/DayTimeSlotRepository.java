package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.DayTimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayTimeSlotRepository extends MongoRepository<DayTimeSlot, String> {
}
