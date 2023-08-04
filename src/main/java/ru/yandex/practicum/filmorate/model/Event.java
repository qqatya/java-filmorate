package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.OperationType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private Integer eventId;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private Integer entityId;

    @NotNull
    private Integer userId;

    @NotNull
    private OperationType operationType;

    @NotNull
    private EventType eventType;

    public Event(Integer eventId, OperationType operationType,
                 EventType eventType, Integer userId, Integer entityId) {
        this.eventId = eventId;
        this.timestamp = LocalDateTime.now();
        this.operationType = operationType;
        this.eventType = eventType;
        this.entityId = entityId;
        this.userId = userId;
    }

    public Event(OperationType operationType, EventType eventType, Integer userId, Integer entityId) {
        this.timestamp = LocalDateTime.now();
        this.operationType = operationType;
        this.eventType = eventType;
        this.entityId = entityId;
        this.userId = userId;
    }

}