package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 500;

    private final DayTimeSlotRepository dayTimeSlotRepository;
    private final ProgramSemesterRepository programSemesterRepository;
    private final LecturerSubjectRepository lecturerSubjectRepository;
    private final RoomRepository roomRepository;

    public GeneticAlgorithm(DayTimeSlotRepository dayTimeSlotRepository,
                            ProgramSemesterRepository programSemesterRepository,
                            LecturerSubjectRepository lecturerSubjectRepository,
                            RoomRepository roomRepository) {
        this.dayTimeSlotRepository = dayTimeSlotRepository;
        this.programSemesterRepository = programSemesterRepository;
        this.lecturerSubjectRepository = lecturerSubjectRepository;
        this.roomRepository = roomRepository;
    }

    public TimeTable run() {
        // ✅ Initialize population using database values
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

            // ✅ Ensure full population size is maintained
            while (newPopulation.size() < POPULATION_SIZE) {
                TimeTable parent1 = selected.get(new Random().nextInt(selected.size()));
                TimeTable parent2 = selected.get(new Random().nextInt(selected.size()));

                // ✅ Perform crossover and mutation
                TimeTable child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                newPopulation.add(child);
            }

            // ✅ Update the population correctly
            population.getTimetables().clear();
            population.getTimetables().addAll(newPopulation);

            // ✅ Find the best fitness score in the current generation
            TimeTable bestTimetable = population.getTimetables()
                    .stream()
                    .max(Comparator.comparingDouble(t -> evaluator.evaluate(t, population.getTimetables())))
                    .orElseThrow(() -> new RuntimeException("No valid timetable found!"));

            System.out.println("Generation " + i + " Best Score: " +
                    evaluator.evaluate(bestTimetable, population.getTimetables()));
        }

        // ✅ Return the best timetable found
        return population.getTimetables()
                .stream()
                .max(Comparator.comparingDouble(t -> evaluator.evaluate(t, population.getTimetables())))
                .orElseThrow(() -> new RuntimeException("No valid timetable found!"));
    }
}
