package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend {

    private Integer id;

    private Boolean isConfirmed;
}
