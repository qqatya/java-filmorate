package ru.yandex.practicum.filmorate.model.type;

import ru.yandex.practicum.filmorate.exception.SearchTypeNotFoundException;

public enum SearchType {
    DIRECTOR("director"),
    TITLE("title"),
    TITLE_DIRECTOR("title,director"),
    DIRECTOR_TITLE("director,title");

    private final String type;

    SearchType(String type) {
        this.type = type;
    }

    public static SearchType of(String code) {
        if (code != null) {
            for (SearchType type : SearchType.values()) {
                if (type.type.equals(code)) {
                    return type;
                }
            }
        }
        throw new SearchTypeNotFoundException(code);
    }
}