package ru.folder.avitoClone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.folder.avitoClone.models.Product;

import java.util.List;

/**
 * Репозиторий для работы с товарами в базе данных.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Поиск товаров по заголовку.
     *
     * @param title Заголовок товара для поиска.
     * @return Список товаров, соответствующих заданному заголовку.
     */
    List<Product> findByTitle(String title);
}
