package com.example.logistics.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.logistics.entity.enums.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
 * Shipments are created and tracked from origin to destination.
 * Relationships:
 *   Shipment (many) -> Customer (1)
 *   Shipment (1) -> Delivery (1)
 *   Shipment (1) -> Tracking (many)
 */

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"customer", "trackingUpdates","delivery"})
public class Shipment extends Auditable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Origin is required")
	@Column(nullable = false)
	private String origin;
	
	@NotBlank(message = "Destination is required")
	@Column(nullable = false)
	private String destination;
	
	@NotNull(message = "Status is required")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(nullable = false)
	private ShipmentStatus status = ShipmentStatus.CREATED;
	
	private Double weightKG;
	
	@NotNull(message = "Customer is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonIgnore
	private Customer customer;
	
	@Builder.Default
	@OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Tracking> trackingUpdates = new ArrayList<>();
	
	@OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private Delivery delivery;
	
	public void addTrackingUpdate(Tracking tracking) {
		trackingUpdates.add(tracking);
		tracking.setShipment(this);
	}
	
	public void setDeliveryRecord(Delivery delivery) {
		this.delivery = delivery;
		delivery.setShipment(this);
	}
}