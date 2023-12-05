package ru.folder.avitoClone.models;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import ru.folder.avitoClone.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.*;

/**
 * Сущность, представляющая пользователя системы.
 */
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "password", length = 1000)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Product> products = new ArrayList<>();

    private LocalDateTime dateOfCreated;

    /**
     * Инициализация даты создания перед сохранением в БД.
     */
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    /**
     * Метод для добавления товара к пользователю.
     *
     * @param product Товар, который необходимо добавить.
     */
    public void addProductToUser(Product product) {
        product.setUser(this);
        products.add(product);
    }

    // -------- Конфигурация Spring Security  -------- //

    /**
     * Проверка, является ли пользователь администратором.
     * @return true, если пользователь является администратором.
     */
    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    // Реализация методов интерфейса UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

}
