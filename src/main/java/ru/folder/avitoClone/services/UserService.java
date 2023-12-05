package ru.folder.avitoClone.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.folder.avitoClone.repositories.UserRepository;
import org.springframework.stereotype.Service;
import ru.folder.avitoClone.models.User;
import ru.folder.avitoClone.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Сервис для работы с пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создать нового пользователя.
     *
     * @param user Данные нового пользователя.
     * @return true, если пользователь успешно создан; false, если пользователь уже существует.
     */
    public boolean createUser(User user) {

        String email = user.getEmail();

        if (userRepository.findByEmail(email) != null) {
            log.error("Данный пользователь уже существует");
            return false;
        }

        log.info("Добавим пользователя");
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
        log.info("Добавлен пользователь с email: {}", email);

        return true;
    }

    /**
     * Получить список всех пользователей.
     *
     * @return Список всех пользователей.
     */
    public List<User> listUser() {

        return userRepository.findAll();

    }

    /**
     * Заблокировать или разблокировать пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     */
    public void banUser(Long id) {

        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Пользователь с id = {}; email: {} заблокирован", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Пользователь с id = {}; email: {} разблокирован", user.getId(), user.getEmail());
            }
            userRepository.save(user);
        }

    }

    /**
     * Изменить роли пользователя.
     *
     * @param user Пользователь, роли которого нужно изменить.
     * @param form Карта, содержащая новые роли пользователя.
     */
    public void changeUserRoles(User user, Map<String, String> form) {

        // Преобразую все роли в строковый вид
        Set<String> roles = Arrays.stream(Role.values())
                // для каждой роли из этой коллекции я вызываю метод name()
                .map(Role::name)
                .collect(Collectors.toSet());

        // очищаю все роли
        user.getRoles().clear();


        for (String key : form.keySet()) {
            // если роль содержит ту роль которая в Map
            if (roles.contains(key)) {
                // добавляем эту роль
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

    }

    /**
     * Получить пользователя по его принципалу.
     *
     * @param principal Принципал пользователя.
     * @return Найденный пользователь или пустой объект User, если principal равен null.
     */
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }


}
