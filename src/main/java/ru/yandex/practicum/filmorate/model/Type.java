package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.TypeNotFoundException;

public enum Type {
    DIRECTOR("director"),
    TITLE("title"),
    TITLE_DIRECTOR("title,director"),
    DIRECTOR_TITLE("director,title");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public static Type of(String code) {
        if (code != null) {
            for (Type type : Type.values()) {
                if (type.type.equals(code)) {
                    return type;
                }
            }
        }
        throw new TypeNotFoundException(code);
    }
}