package com.journal.backendJournal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "entry_results")
public class EntryResultEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entry", columnDefinition = "TEXT") 
	private String entry;

	@Column(name = "results", length = 50)
	private String results;

	// Getters and Setters
	public Long getId() {
	return id;
	}

	public void setId(Long id) {
	this.id = id;
	}

	public String getEntry() {
	return entry;
	}

	public void setEntry(String entry) {
	this.entry = entry;
	}

	public String getResults() {
	return results;
	}

	public void setResults(String results) {
	this.results = results;
	}
}
