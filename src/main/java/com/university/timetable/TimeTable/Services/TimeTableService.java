package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.*;
import com.university.timetable.TimeTable.TimeTableGeneration.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 500;

    private final TimeTableRepository timeTableRepository;
    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;

    public TimeTable runGeneticAlgorithm() {

        // ✅ Initialize population with real-time database values
        Population population = new Population(
                POPULATION_SIZE,
                dayTimeSlotRepository,
                programSemesterRepository,
                lecturerSubjectRepository,
                roomRepository
        );

        Selection selection = new Selection();
        Crossover crossover = new Crossover();
        Mutation mutation = new Mutation();
        FitnessEvaluator evaluator = new FitnessEvaluator();

        for (int i = 0; i < GENERATIONS; i++) {
            // ✅ Select the best individuals
            List<TimeTable> selected = selection.selectBest(population);
            List<TimeTable> newPopulation = new ArrayList<>();

            // ✅ Ensure new population has the correct size
            while (newPopulation.size() < POPULATION_SIZE) {
                TimeTable parent1 = selected.get(new Random().nextInt(selected.size()));
                TimeTable parent2 = selected.get(new Random().nextInt(selected.size()));

                // ✅ Perform crossover and mutation
                TimeTable child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                newPopulation.add(child);
            }

            // ✅ Update the population without resetting it
            population.getTimetables().clear();
            population.getTimetables().addAll(newPopulation);
        }

        // ✅ Get the best timetable based on fitness evaluation
        TimeTable bestTimetable = population.getTimetables()
                .stream()
                .max(Comparator.comparingDouble(t -> evaluator.evaluate(t, population.getTimetables())))
                .orElseThrow(() -> new RuntimeException("No valid timetable found!"));

        // ✅ Save the best timetable
        timeTableRepository.save(bestTimetable);

        return bestTimetable;
    }
}
