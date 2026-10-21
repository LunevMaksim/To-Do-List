package org.lunev.todo.dto;

import java.time.LocalDateTime;

public record TaskCreateDto(Long userId, String name, String description,
                            String category, LocalDateTime deadline) {
}
