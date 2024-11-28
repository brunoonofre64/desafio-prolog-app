package com.prologapp.desafio.presentation.v1.handler;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    private String error;
    private LocalDateTime timestamp;
    private int codeStatus;
    private String message;
    private List<String> details;
}
