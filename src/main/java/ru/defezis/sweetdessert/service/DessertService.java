package ru.defezis.sweetdessert.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.defezis.sweetdessert.data.DessertDataService;
import ru.defezis.sweetdessert.model.Dessert;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertDataService dataService;

    public List<Dessert> getAll() {
        return dataService.getAll();
    }

    /**
     * Метод вернет Десерт по заданному идентификатору.
     *
     * @param dessertId идентификатор Десерта
     * @return модель Десерта
     * @throws RuntimeException если Десерт с таким id не найден
     */
    public Dessert getDessert(long dessertId) {
        Dessert result = dataService.getById(dessertId);
        if (Objects.nonNull(result)) {
            return result;
        } else {
            throw new RuntimeException("Dessert id must be prefer to dessert");
        }
    }

    public Long create(Dessert dessertNew) {
        throw new UnsupportedOperationException();
    }

    public void update(Dessert dessertNew, long dessertId) {
        throw new UnsupportedOperationException();
    }

    public void delete(long dessertId) {
        throw new UnsupportedOperationException();
    }
}
