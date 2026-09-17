package com.marumiru.mylog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Getter 
@NoArgsConstructor 
@Table(name="role")
public class Role {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_id", updatable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rolename; // "ROLE_USER", "ROLE_ADMIN"

    public Role(String rolename) {
        this.rolename = rolename;
    }
}
