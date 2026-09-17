package com.marumiru.mylog.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "event")
@Getter 
@Setter 
@Entity 
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", updatable = false)
    private Long id; // pk

    @Column(nullable = false, length=100)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne 
    @JoinColumn(name="venue_id", referencedColumnName = "venue_id")
    Venue venue; 

}
