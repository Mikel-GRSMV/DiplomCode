package ru.folder.avitoClone.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import ru.folder.avitoClone.repositories.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * Реализация интерфейса UserDetailsService для аутентификации пользователей Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Метод для загрузки пользователя по его электронной почте.
     *
     * @param email Электронная почта пользователя.
     * @return UserDetails объект, представляющий пользователя в Spring Security.
     * @throws UsernameNotFoundException Возникает, если пользователь с указанным email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

}
