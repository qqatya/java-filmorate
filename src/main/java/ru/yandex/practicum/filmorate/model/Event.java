package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.OperationType;

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
    private OperationType operationType;

    @NotNull
    private EventType event;

    public Event(OperationType operationType, EventType event, Integer userId, Integer entityId) {
        this.timestamp = Instant.now().toEpochMilli();
        this.operationType = operationType;
        this.event = event;
        this.entityId = entityId;
        this.userId = userId;
    }

}