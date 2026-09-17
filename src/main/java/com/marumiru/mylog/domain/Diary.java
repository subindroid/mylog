package com.marumiru.mylog.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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

@Table(name = "diary")
@Getter 
@Setter 
@Entity 
public class Diary {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="diary_id")
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 2000)
    private String content;

    @CreationTimestamp
    private LocalDateTime writeDate;

    @ManyToOne 
    @JoinColumn(name="user_id")
    User user; 

    @ManyToOne
    @JoinColumn(name="event_id")
    Event event;
}
