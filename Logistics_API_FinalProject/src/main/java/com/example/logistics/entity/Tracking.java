package com.example.logistics.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Tracking updates provide real-time location and status of shipments.
 * Relationship: Tracking (many) -> Shipment (1)
 */

@Entity
@Table(name = "tracking_updates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "shipment")
public class Tracking extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Current location is required")
	@Column(nullable = false)
	private String currentLocation;
	
	@NotBlank(message = "status description is required")
	@Column(nullable = false)
	private String statusDescription;
	
	@Builder.Default
	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt = LocalDateTime.now();
	
	@NotNull(message = "Shipment is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipment_id", nullable = false)
	private Shipment shipment;
}