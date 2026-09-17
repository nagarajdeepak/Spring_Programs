package com.example.logistics.entity;

import java.time.LocalDateTime;

import com.example.logistics.entity.enums.DeliveryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Deliveries are recorded upon successful shipment completion.
 * Relationship: Delivery (1) -> Shipment (1)
 */

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "shipment")
public class Delivery extends Auditable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String receivedBy;
	
	@NotNull(message = "Delivery status is required")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(nullable = false)
	private DeliveryStatus status = DeliveryStatus.PENDING;
	
	private LocalDateTime deliveredAt;
	
	private String signatureNote;
	
	@NotNull(message = "Shipment is required")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipment_id", nullable = false, unique = true)
	private Shipment shipment;
}