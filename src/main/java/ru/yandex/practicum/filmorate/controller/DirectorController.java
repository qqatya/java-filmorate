package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    /**
     * Создание режиссера
     *
     * @param director Объект, содержащий данные для создания
     * @return Созданный режиссер
     */
    @PostMapping
    public Director createUser(@Valid @RequestBody Director director) { //POST /directors - Создание режиссёра
        return directorService.createDirector(director);
    }

    /**
     * Обновление режиссера
     *
     * @param director Объект, содержащий данные для обновления
     * @return Обновленный режиссер
     */
    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) { //PUT /directors - Изменение режиссёра
        return directorService.updateDirector(director);
    }

    /**
     * Получение всех режиссеров
     *
     * @return Список режиссеров
     */
    @GetMapping
    public List<Director> getAllDirectors() { //GET /directors - Список всех режиссёров
        return directorService.getAllDirectors();
    }

    /**
     * Получение режиссёра по id
     *
     * @param id Идентификатор режиссёра
     * @return режиссёр
     */
    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) { //GET /directors/{id}- Получение режиссёра по id
        return directorService.getDirectorById(id);
    }

    /**
     * Удаление режиссера по идентификатору
     *
     * @param id Идентификатор режиссера
     */
    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable Integer id) {
        directorService.deleteDirectorById(id);
    }
}