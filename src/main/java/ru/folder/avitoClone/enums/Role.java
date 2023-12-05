package ru.folder.avitoClone.enums;

import org.springframework.security.core.GrantedAuthority;
import lombok.Getter;

/**
 * Перечисление, представляющее роли пользователей в системе.
 * Реализует интерфейс GrantedAuthority для использования в Spring Security.
 * <p>
 * Enum Role представляет роли пользователей в системе и может использоваться в Spring Security
 * для определения их прав доступа.
 */
@Getter
public enum Role implements GrantedAuthority {

    ROLE_USER, ROLE_ADMIN;

    /**
     * Получить строковое представление роли.
     *
     * @return Строковое представление роли.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}

