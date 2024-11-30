package com.example.backend.enity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String link_image;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime created_at;
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updated_at;
}
