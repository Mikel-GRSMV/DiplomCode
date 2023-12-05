package ru.folder.avitoClone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.folder.avitoClone.models.User;

/**
 * Репозиторий для работы с пользователями в базе данных.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по электронной почте.
     *
     * @param email Адрес электронной почты для поиска пользователя.
     * @return Найденный пользователь или null, если пользователь не найден.
     */
    User findByEmail(String email);

}
