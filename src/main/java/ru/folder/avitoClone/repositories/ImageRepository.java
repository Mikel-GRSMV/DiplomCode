package ru.folder.avitoClone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.folder.avitoClone.models.Image;

/**
 * Репозиторий для работы с изображениями в базе данных.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
}
