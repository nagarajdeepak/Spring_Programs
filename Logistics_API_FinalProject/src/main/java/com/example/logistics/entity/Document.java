package com.example.logistics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
 * Metadata for a file attached to a shipment (invoice, shipping label,
 * proof-of-delivery photo, etc). The actual bytes live on disk (or any
 * FileStorageService implementation) - this row only tracks where to find
 * them and how to present them back to a client.
 * Relationship: Document (many) -> Shipment (1)
 */

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "shipment")
public class Document extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name = "original_file_name", nullable =false)
	private String originalFileName;
	
	/** UUID-prefixed name actually used on disk - avoids collisions and path traversal. */
	@NotBlank
	@Column(name = "stored_file_name", nullable = false, unique = true)
	private String storedFileName;
	
	private String contentType;
	
	private Long fileSize;
	
	@NotNull(message = "Shipment is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipment_id", nullable = false)
	@JsonIgnore
	private Shipment shipment;
}