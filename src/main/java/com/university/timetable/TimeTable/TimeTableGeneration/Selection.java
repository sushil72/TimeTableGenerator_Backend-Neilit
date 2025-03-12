package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Selection {
    private static final int SELECTION_SIZE = 5; // Number of timetables to select
    private final FitnessEvaluator evaluator;

    public Selection(FitnessEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public List<List<TimeTable>> selectBest(Population population) {
        List<List<TimeTable>> populationList = population.getPopulation();

        // Ensure we don't try to select more than available
        int limitSize = Math.min(SELECTION_SIZE, populationList.size());

        // Calculate fitness for each timetable
        List<TimetableWithFitness> fitnessScores = new ArrayList<>();

        for (List<TimeTable> timetable : populationList) {
            double totalFitness = 0;
            // Calculate total fitness by summing individual entries
            for (TimeTable entry : timetable) {
                totalFitness += evaluator.evaluate(entry, timetable);
            }
            fitnessScores.add(new TimetableWithFitness(timetable, totalFitness));
        }

        // Sort by fitness (descending) and select the best ones
        return fitnessScores.stream()
                .sorted(Comparator.comparingDouble(TimetableWithFitness::getFitness).reversed())
                .limit(limitSize)
                .map(TimetableWithFitness::getTimetable)
                .collect(Collectors.toList());
    }

    // Helper class to track fitness with timetable
    private static class TimetableWithFitness {
        private final List<TimeTable> timetable;
        private final double fitness;

        public TimetableWithFitness(List<TimeTable> timetable, double fitness) {
            this.timetable = timetable;
            this.fitness = fitness;
        }

        public List<TimeTable> getTimetable() {
            return timetable;
        }

        public double getFitness() {
            return fitness;
        }
    }
}