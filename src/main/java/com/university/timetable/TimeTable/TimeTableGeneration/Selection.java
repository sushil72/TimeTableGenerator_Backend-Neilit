package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Selection {
    private static final int SELECTION_SIZE = 50; // ✅ Configurable selection size
    private final FitnessEvaluator evaluator = new FitnessEvaluator(); // ✅ Instantiate once

    public List<TimeTable> selectBest(Population population) {
        return population.getTimetables()
                .stream()
                .sorted(Comparator.comparingDouble(t -> evaluator.evaluate((TimeTable) t, population.getTimetables())) // ✅ Pass population list
                        .reversed()) // ✅ Higher fitness is better
                .limit(SELECTION_SIZE) // ✅ Use configurable size
                .collect(Collectors.toList());
    }
}
