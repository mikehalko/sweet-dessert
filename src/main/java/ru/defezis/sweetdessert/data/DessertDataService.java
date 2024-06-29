package ru.defezis.sweetdessert.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.defezis.sweetdessert.exception.IncorrectDataException;
import ru.defezis.sweetdessert.model.Dessert;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DessertDataService {

    private static long ids = 1;
    private static final List<Dessert> dessertList = new ArrayList<>(List.of(
            Dessert.builder().id(ids++).name("Cheesecake").build(),
            Dessert.builder().id(ids++).name("Ice cream").build(),
            Dessert.builder().id(ids++).name("Tiramisu").build(),
            Dessert.builder().id(ids++).name("Brownies").build(),
            Dessert.builder().id(ids++).name("Pecan pie").build(),
            Dessert.builder().id(ids++).name("Chocolate").build(),
            Dessert.builder().id(ids++).name("Madeira cake").build(),
            Dessert.builder().id(ids++).name("Banana pudding").build()
    ));

    /**
     * Получить список всех Десертов.
     *
     * @return список десертов
     */
    @NotNull
    public List<Dessert> list() {
        return List.copyOf(dessertList);
    }

    /**
     * Найти Десерт по заданному идентификатору.
     *
     * @param id идентификатор Десерта
     * @return модель Десерта, либо null, если Десерт с таким id не найден
     */
    @Nullable
    public Dessert getById(long id) {
        return dessertList.stream()
                .filter(dessert -> dessert.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Добавить новый Десерт.
     *
     * @param dessert десерт
     * @return идентификатор сохраненного десерта
     */
    @NotNull
    public Long create(@NotNull Dessert dessert) {
        dessert.setId(ids++);
        dessertList.add(dessert);
        log.info("Created Dessert with id {}", dessert.getId());
        return dessert.getId();
    }

    /**
     * Обновить существующий Десерт.
     *
     * @param updated Десерт для обновления
     * @return обновленный Десерт
     * @throws IncorrectDataException если десерт не найден по идентификатору
     */
    public Dessert update(@NotNull Dessert updated) {
        Dessert dessert = dessertList.stream()
                .filter(item -> item.getId().equals(updated.getId()))
                .findFirst()
                .orElseThrow(() -> new IncorrectDataException("Dessert not found"));

        dessert.setName(updated.getName());
        dessert.setIngredients(updated.getIngredients());
        log.debug("Updated dessert with id: {}", dessert.getId());

        return dessert;
    }

    /**
     * Удалить Десерт из списка по заданному идентификатору.
     *
     * @param id идентификатор
     */
    public void removeById(long id) {
        boolean removed = dessertList.removeIf(dessert -> dessert.getId().equals(id));
        if (removed) {
            log.debug("Removed dessert with id: {}", id);
        } else {
            log.warn("Failed to remove dessert with id: {}", id);
        }
    }
}
