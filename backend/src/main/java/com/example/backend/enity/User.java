package com.example.backend.enity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private String is_activated;
    @Column(name = "is_deleted", columnDefinition = "INT")
    private int isDeleted;
    private Set<String> role; // chỉ tô tại 1

}
