package com.example.logistics.service.impl;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.logistics.entity.Shipment;
import com.example.logistics.entity.enums.ShipmentStatus;
import com.example.logistics.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogisticsSchedulerService {

    private final ShipmentRepository shipmentRepository;

    /**
     * Runs every hour (3,600,000 ms) to query newly created shipments.
     */
    @Scheduled(fixedRate = 3600000)
    public void monitorPendingShipments() {
        log.info("[SCHEDULED TASK] Checking for shipments with CREATED status...");
        
        List<Shipment> createdShipments = shipmentRepository.findByStatus(ShipmentStatus.CREATED);
        log.info("[SCHEDULED TASK] Found {} pending shipments awaiting dispatch.", createdShipments.size());
    }

    /**
     * Runs every midnight at 00:00:00 to log daily system metrics.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyNightlyMaintenance() {
        log.info("[SCHEDULED TASK] Running daily system maintenance and log check...");
        
        long totalShipments = shipmentRepository.count();
        log.info("[SCHEDULED TASK] Total shipments logged in database: {}", totalShipments);
    }
}