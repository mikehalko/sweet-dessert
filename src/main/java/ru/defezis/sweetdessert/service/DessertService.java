package ru.defezis.sweetdessert.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

    /**
     * Получить список всех Десертов.
     *
     * @return список десертов
     */
    @NotNull
    public List<Dessert> getAll() {
        return dataService.list();
    }

    /**
     * Метод вернет Десерт по заданному идентификатору.
     *
     * @param dessertId идентификатор Десерта
     * @return модель Десерта
     * @throws RuntimeException если Десерт с таким id не найден
     */
    @NotNull
    public Dessert getDessert(long dessertId) {
        Dessert result = dataService.getById(dessertId);
        if (Objects.nonNull(result)) {
            return result;
        } else {
            throw new RuntimeException("Dessert id must be prefer to dessert"); // TODO exception
        }
    }

    /**
     * Сохранить новый Десерт.
     *
     * @param dessertNew новый Десерт
     * @return идентификатор сохраненного десерта
     */
    @NotNull
    public Long create(@NotNull Dessert dessertNew) {
        return dataService.create(dessertNew);
    }

    /**
     * Обновить существующий Десерт.
     *
     * @param dessertNew Десерт с данными для обновления
     * @param dessertId идентификатор Десерта для обновления
     */
    public void update(@NotNull Dessert dessertNew, long dessertId) {
        dessertNew.setId(dessertId);
        dataService.update(dessertNew);
    }

    /**
     * Удалить Десерт по заданному идентификатору.
     *
     * @param dessertId идентификатор удаляемого Десерта
     */
    public void delete(long dessertId) {
        dataService.removeById(dessertId);
    }
}
