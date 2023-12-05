package ru.folder.avitoClone.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.folder.avitoClone.enums.Role;
import ru.folder.avitoClone.models.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service
@Slf4j
public class ServiceUser {

//    private static ServiceUser INSTANCE;
    private final SessionFactory sessionFactory;
//
    private ServiceUser(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
//
//    public static ServiceUser getInstance(SessionFactory sessionFactory) {
//        if (INSTANCE == null) {
//            INSTANCE = new ServiceUser(sessionFactory);
//        }
//        return INSTANCE;
//    }

    /**
     * Создать нового пользователя.
     *
     * @param user Данные нового пользователя.
     * @return true, если пользователь успешно создан; false, если пользователь уже существует.
     */
    public boolean createUser(User user) {

        String email = user.getEmail();

        if (findByEmail(email) != null) {
            log.error("Данный пользователь уже существует");
            return false;
        }

        log.info("Добавим пользователя");
        user.setActive(true);
        user.setPassword(user.getPassword());
        user.getRoles().add(Role.ROLE_USER);
        saveUser(user);
        log.info("Добавлен пользователь с email: {}", email);

        return true;
    }

    private User findByEmail(String email) {

        User userResult = null;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);

            criteriaQuery
                    .select(userRoot)
                    .where(criteriaBuilder.equal(userRoot.get("email"), email));

            userResult = session
                    .createQuery(criteriaQuery)
                    .setMaxResults(1)
                    .uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userResult;
    }

    private User saveUser(User entity) {
        try (Session session = sessionFactory.openSession()) {
            // Начать транзакцию
            session.beginTransaction();

            // Сохранить или обновить сущность
            session.saveOrUpdate(entity);

            // Закоммитить транзакцию
            session.getTransaction().commit();

            return entity;
        } catch (Exception e) {
            // Обработка ошибок
            e.printStackTrace();
            return null;
        }
    }
}
