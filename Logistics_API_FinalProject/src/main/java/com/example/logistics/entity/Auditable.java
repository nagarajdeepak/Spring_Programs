package com.example.logistics.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Common auditing fields shared by every entity.
 * @EntityListeners(AuditingEntityListener.class) plugs into Spring Data JPA's
 * auditing infrastructure (enabled via @EnableJpaAuditing) to populate these
 * columns automatically on persist/update.
 */

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
	
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonIgnore
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at")
	@JsonIgnore
	private LocalDateTime updatedAt;
}