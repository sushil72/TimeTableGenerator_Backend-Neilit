package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneticAlgorithm {
    private final int populationSize;
    private final int generations;
    private final Population population;
    private final Selection selection;
    private final Crossover crossover;
    private final Mutation mutation;
    private final FitnessEvaluator evaluator;
    private final Random random = new Random();

    public GeneticAlgorithm(@Value("${population.size:20}") int populationSize,
                            @Value("${genetic.generations:100}") int generations,
                            Population population,
                            Selection selection,
                            Crossover crossover,
                            Mutation mutation,
                            FitnessEvaluator evaluator) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.population = population;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.evaluator = evaluator;
    }

    public List<TimeTable> run() {
        System.out.println("🚀 Starting Genetic Algorithm for Timetable Optimization...");
        List<List<TimeTable>> currentPopulation = population.getPopulation();

        // Track the best solution across generations
        List<TimeTable> bestSolution = null;
        double bestFitness = Double.NEGATIVE_INFINITY;

        for (int gen = 0; gen < generations; gen++) {
            System.out.println("🔄 Generation " + gen + " - Evaluating Timetables...");

            // Select the best individuals for crossover
            List<List<TimeTable>> selected = selection.selectBest(population);

            if (selected.size() < 2) {
                System.out.println("⚠️ Warning: Not enough valid timetables to perform crossover! Using available ones.");
                if (selected.isEmpty()) {
                    throw new RuntimeException("❌ No valid timetables found for selection!");
                }
            }

            List<List<TimeTable>> newPopulation = new ArrayList<>();

            // Always keep the best solution from the previous generation (elitism)
            if (bestSolution != null) {
                newPopulation.add(new ArrayList<>(bestSolution));
            }

            // Generate new population through crossover and mutation
            while (newPopulation.size() < populationSize) {
                // Select parents using tournament selection for more diversity
                List<TimeTable> parent1 = tournamentSelect(selected);
                List<TimeTable> parent2 = tournamentSelect(selected);

                // Avoid self-crossover if possible
                int attempts = 0;
                while (parent1.equals(parent2) && selected.size() > 1 && attempts < 5) {
                    parent2 = tournamentSelect(selected);
                    attempts++;
                }

                try {
                    // Perform crossover
                    List<TimeTable> child = crossover.crossover(parent1, parent2, currentPopulation);

                    // Perform mutation
                    mutation.mutate(child, currentPopulation);

                    // Add to new population
                    newPopulation.add(child);
                } catch (Exception e) {
                    System.out.println("⚠️ Error during crossover/mutation: " + e.getMessage());
                    // If crossover fails, add a copy of a parent to maintain population size
                    newPopulation.add(new ArrayList<>(parent1));
                }
            }

            // Update current population
            currentPopulation = newPopulation;
            population.setPopulation(newPopulation);

            // Find the best timetable in this generation
            List<TimeTable> generationBest = findBestTimetable(currentPopulation);
            double generationBestFitness = calculateTotalFitness(generationBest);

            System.out.println("✅ Generation " + gen + " - Best Fitness Score: " + generationBestFitness);

            // Update overall best if this generation has a better solution
            if (generationBestFitness > bestFitness) {
                bestFitness = generationBestFitness;
                bestSolution = new ArrayList<>(generationBest);
                System.out.println("🌟 New best solution found! Fitness: " + bestFitness);
            }

            // Early termination if fitness is high enough
            if (bestFitness > populationSize * 15) { // Adjust threshold based on your fitness function
                System.out.println("🎯 High-quality timetable found, stopping early!");
                break;
            }
        }

        // Return the best overall timetable after evolution
        return bestSolution != null ? bestSolution : findBestTimetable(currentPopulation);
    }

    // Tournament selection - select the best from a random subset
    private List<TimeTable> tournamentSelect(List<List<TimeTable>> selected) {
        if (selected.size() <= 1) {
            return selected.get(0);
        }

        int tournamentSize = Math.min(3, selected.size());
        List<List<TimeTable>> tournament = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(selected.get(random.nextInt(selected.size())));
        }

        return tournament.stream()
                .max(Comparator.comparingDouble(this::calculateTotalFitness))
                .orElse(selected.get(0));
    }

    // Calculate total fitness for a timetable
    private double calculateTotalFitness(List<TimeTable> timetable) {
        return timetable.stream()
                .mapToDouble(entry -> evaluator.evaluate(entry, timetable))
                .sum();
    }

    // Find the best timetable in the population
    private List<TimeTable> findBestTimetable(List<List<TimeTable>> population) {
        return population.stream()
                .max(Comparator.comparingDouble(this::calculateTotalFitness))
                .orElseThrow(() -> new RuntimeException("❌ No valid timetable found!"));
    }
}