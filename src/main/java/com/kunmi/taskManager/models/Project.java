package com.kunmi.taskManager.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Project(String name, LocalDateTime createDate, User user) {
        this.name = name;
        this.createDate = createDate;
        this.user = user;
    }

}
