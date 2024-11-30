package com.example.backend.dto.request.Feedback;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RattingRequest {

    private Long userId;
    private Long feedbackId;
    private Long postId;
    private Integer liked;
}
