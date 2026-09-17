package com.example.logistics.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customers register and manage their own shipment requests.
 * Relationship: Customer (1) -> Shipment (many)
 */

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends Auditable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Customer name is required")
	@Column(nullable = false)
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid address")
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotBlank(message = "Phone number is required")
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	private String address;
	
	@Builder.Default
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Shipment> shipments = new ArrayList<>();
	
	public void addShipment(Shipment shipment) {
		shipments.add(shipment);
		shipment.setCustomer(this);
	}
}