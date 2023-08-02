package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.validation.DirectorValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;
    private final DirectorValidator directorValidator;

    @Override
    public Director createDirector(Director director) {
        directorValidator.validate(director);
        return directorRepository.insertDirector(director);
    }

    @Override
    public Director updateDirector(Director director) {
        directorValidator.validate(director);
        if (directorRepository.doesExist(director.getId())) {
            log.info("Updating directorId = {}", director.getId());
            return directorRepository.updateDirector(director);
        }
        throw new DirectorNotFoundException(String.valueOf(director.getId()));
    }

    @Override
    public List<Director> getAllDirectors() {
        log.info("Returning all directors");
        return directorRepository.getAllDirectors();
    }

    @Override
    public Director getDirectorById(Integer id) {
        log.info("Getting directorId = {}", id);
        return directorRepository.getDirectorById(id)
                .orElseThrow(() -> new DirectorNotFoundException(String.valueOf(id)));
    }

    @Override
    public void deleteDirectorById(Integer id) {
        if (!directorRepository.doesExist(id)) {
            throw new DirectorNotFoundException(String.valueOf(id));
        }
        log.info("Deleting directorId = {}", id);
        directorRepository.deleteDirectorById(id);
    }
}