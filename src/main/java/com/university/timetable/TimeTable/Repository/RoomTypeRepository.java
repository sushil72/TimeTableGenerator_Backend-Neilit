package com.university.timetable.TimeTable.Repository;

import com.university.timetable.TimeTable.Entity.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends MongoRepository<RoomType, String> {
}
