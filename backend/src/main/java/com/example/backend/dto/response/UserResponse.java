package com.example.backend.dto.response;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
     private int id;
     private String username;
     private String password;
     private String email;
     private Set<String> role;
}
