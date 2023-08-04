package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.OperationType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Integer eventId;
    @NotNull
    private Long timestamp;
    @Positive
    @NotNull
    private Integer entityId;
    @Positive
    @NotNull
    private Integer userId;
    @NotNull
    private OperationType operationType;
    @NotNull
    private EventType eventType;

    public Event(Integer id, Long eventTimestamp, OperationType operationType, EventType eventType, Integer userId, Integer entityId) {
        this.timestamp = Instant.now().toEpochMilli();
        this.operationType = operationType;
        this.eventType = eventType;
        this.entityId = entityId;
        this.userId = userId;
    }

    public Event(OperationType operationType, EventType eventType, Integer id, Integer userId) {
        this.timestamp = Instant.now().toEpochMilli();
        this.operationType = operationType;
        this.eventType = eventType;
        this.entityId = entityId;
        this.userId = userId;
    }
}