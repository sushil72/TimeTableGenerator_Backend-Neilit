package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.*;
import com.university.timetable.TimeTable.TimeTableGeneration.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Slf4j // ✅ Enables logging
@Service
@RequiredArgsConstructor
public class TimeTableService {

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = Math.min(POPULATION_SIZE / 2, 5);

    private final TimeTableRepository timeTableRepository;
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;
    private final Selection selection;
    private final Crossover crossover;
    private final Mutation mutation;
    private final FitnessEvaluator evaluator;

    public List<TimeTable> runGeneticAlgorithm() {
        log.info("🚀 Starting Genetic Algorithm for Timetable Generation...");

        // ✅ Initialize population
        Population population = new Population(
                POPULATION_SIZE,
                dayTimeSlotRepository,
                programSemesterRepository,
                lecturerSubjectRepository,
                roomRepository
        );

        for (int i = 0; i < GENERATIONS; i++) {
            log.info("🔄 Generation {} - Evaluating Timetables...", i);

            List<List<TimeTable>> selected = selection.selectBest(population);

            // 🛑 Prevent infinite loop if selection fails
            if (selected.isEmpty()) {
                log.error("❌ Selection process failed: No valid timetables found. Stopping execution.");
                throw new RuntimeException("Selection process failed: No valid timetables found.");
            }

            List<List<TimeTable>> newPopulation = new java.util.ArrayList<>();

            while (newPopulation.size() < POPULATION_SIZE) {
                List<TimeTable> parent1 = selected.get(new Random().nextInt(selected.size()));
                List<TimeTable> parent2 = selected.get(new Random().nextInt(selected.size()));

                // Ensure parents are not the same
                while (parent1.equals(parent2) && selected.size() > 1) {
                    parent2 = selected.get(new Random().nextInt(selected.size()));
                }

                // ✅ Perform crossover on full timetables
                List<TimeTable> child = crossover.crossover(parent1, parent2, population.getPopulation());

                // ✅ Apply mutation based on existing schedules
                mutation.mutate(child, population.getPopulation());

                newPopulation.add(child);
            }

            // ✅ Update population with new generation
            population.setPopulation(newPopulation);

            // ✅ Log best fitness score of the generation
            List<TimeTable> bestInGeneration = findBestTimetable(newPopulation);
            double bestFitness = calculateTotalFitness(bestInGeneration);

            log.info("✅ Generation {} - Best Fitness Score: {}", i, bestFitness);

            // 🛑 Early exit if a highly fit timetable is found
            if (bestFitness > population.getPopulation().size() * 15) {
                log.info("🎉 Found a near-optimal solution, stopping early!");
                break;
            }
        }

        log.info("✅ Population size after evolution: {}", population.getPopulation().size());

        // ✅ Get the best timetable from the final population
        List<TimeTable> bestTimetable = findBestTimetable(population.getPopulation());

        // ✅ Save best timetable to DB
        timeTableRepository.saveAll(bestTimetable);
        log.info("🎯 Best Generated Timetable saved to DB.");

        return bestTimetable;
    }

    // ✅ Find the best timetable in the population
    private List<TimeTable> findBestTimetable(List<List<TimeTable>> population) {
        return population.stream()
                .max(Comparator.comparingDouble(this::calculateTotalFitness))
                .orElseThrow(() -> new RuntimeException("❌ No valid timetable found!"));
    }

    // ✅ Calculate total fitness for a timetable
    private double calculateTotalFitness(List<TimeTable> timetable) {
        return timetable.stream()
                .mapToDouble(entry -> evaluator.evaluate(entry, timetable))
                .sum();
    }
}
