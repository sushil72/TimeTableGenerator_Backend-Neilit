package com.university.timetable.TimeTable.TimeTableGeneration;

import com.university.timetable.TimeTable.Entity.TimeTable;
import com.university.timetable.TimeTable.Repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Handles asynchronous timetable generation for potentially long-running processes
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncTimeTableGenerator {

    private final CSPBacktrackingSolver solver;
    private final TimeTableRepository timeTableRepository;
    private final ConsistencyChecker consistencyChecker;

    /**
     * Generates a timetable asynchronously
     *
     * @param clearExisting Whether to clear existing timetable entries
     * @return CompletableFuture with the generated timetable
     */
    @Async("timetableTaskExecutor")
    public CompletableFuture<GenerationResult> generateTimetableAsync(boolean clearExisting) {
        log.info("Starting asynchronous timetable generation");
        try {
            // Clear existing if requested
            if (clearExisting) {
                log.info("Clearing existing timetable data");
                timeTableRepository.deleteAll();
            }

            // Generate new timetable
            List<TimeTable> timetable = solver.solve();

            // Check constraints
            List<String> violations = consistencyChecker.checkAllConstraints(timetable);

            // Save result
            if (!violations.isEmpty()) {
                log.warn("Generated timetable has {} constraint violations", violations.size());
            } else {
                log.info("Successfully generated timetable with no violations");
            }

            timeTableRepository.saveAll(timetable);

            return CompletableFuture.completedFuture(new GenerationResult(timetable, violations));
        } catch (Exception e) {
            log.error("Timetable generation failed: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Result holder for timetable generation
     */
    public static class GenerationResult {
        private final List<TimeTable> timetable;
        private final List<String> violations;
        private final boolean success;

        public GenerationResult(List<TimeTable> timetable, List<String> violations) {
            this.timetable = timetable;
            this.violations = violations;
            this.success = true;
        }

        public List<TimeTable> getTimetable() {
            return timetable;
        }

        public List<String> getViolations() {
            return violations;
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean hasViolations() {
            return !violations.isEmpty();
        }
    }
}