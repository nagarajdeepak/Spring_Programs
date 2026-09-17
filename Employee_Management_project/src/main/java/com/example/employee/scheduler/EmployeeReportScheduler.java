package com.example.employee.scheduler;

import com.example.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background jobs related to employee data.
 * Demonstrates Spring's {@code @Scheduled} support (enabled via
 * {@code @EnableScheduling} on the main application class).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeReportScheduler {

    private final EmployeeRepository employeeRepository;

    /**
     * Logs the total headcount every hour, on the hour.
     * Cron: second, minute, hour, day-of-month, month, day-of-week.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void logHeadcountReport() {
        long total = employeeRepository.count();
        log.info("[Scheduled Report] Current total employee headcount: {}", total);
    }

    /**
     * Lightweight heartbeat every 5 minutes so it's obvious the scheduler is alive
     * without waiting for the top of the hour.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void heartbeat() {
        log.debug("[Scheduler Heartbeat] Employee management scheduler is running");
    }
}
