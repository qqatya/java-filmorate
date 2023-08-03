package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.model.eventenum.Entity;
import ru.yandex.practicum.filmorate.model.eventenum.Operation;

import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Integer eventId;
    @NonNull
    private Long timestamp;
    @Positive
    @NonNull
    private Integer entityId;
    @Positive
    @NonNull
    private Integer userId;
    @NonNull
    private Operation operation;
    @NonNull
    private Entity eventType;

    public Event(int id, long eventTimestamp, Operation operation, Entity entity, int userId, int entityId) {
        if (userId <= 0) {
            throw new ValidationException("userId должен быть больше 0");
        }
        if (entityId <= 0) {
            throw new ValidationException("Сущность не может быть меньше или равна 0");
        }
        if (operation == null) {
            throw new ValidationException("Не может быть не совершена никакая операция");
        }
        if (entity == null) {
            throw new ValidationException("Должно произойти одно из событий: Лайк, Отзыв, Действие с другом");
        }
        this.timestamp = Instant.now().toEpochMilli();
        this.operation = operation;
        this.eventType = entity;
        this.entityId = entityId;
        this.userId = userId;
    }

    public Event(Operation operation, Entity entity, Integer id, Integer userId) {
    }
}