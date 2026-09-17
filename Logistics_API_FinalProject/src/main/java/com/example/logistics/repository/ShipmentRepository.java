package com.example.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.logistics.entity.Shipment;
import com.example.logistics.entity.enums.ShipmentStatus;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {

    List<Shipment> findByCustomerId(Long customerId);

    List<Shipment> findByStatus(ShipmentStatus status);
}
