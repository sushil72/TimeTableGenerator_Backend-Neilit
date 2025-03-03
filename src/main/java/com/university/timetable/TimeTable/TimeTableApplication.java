package com.university.timetable.TimeTable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.university.timetable.TimeTable.Repository")
public class TimeTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeTableApplication.class, args);
	}

}
