package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.Operation;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private Integer eventId;

    @NotNull
    private Long timestamp;

    @NotNull
    private Integer entityId;

    @NotNull
    private Integer userId;

    @NotNull
    private Operation operation;

    @NotNull
    private EventType eventType;

    public Event(Integer userId, EventType eventType, Operation operation, Integer entityId) {
        this.timestamp = Instant.now().toEpochMilli();
        this.operation = operation;
        this.eventType = eventType;
        this.entityId = entityId;
        this.userId = userId;
    }

}