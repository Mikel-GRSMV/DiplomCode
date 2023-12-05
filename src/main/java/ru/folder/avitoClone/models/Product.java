package ru.folder.avitoClone.models;

import lombok.Data;

import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая товар в системе.
 */
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "city")
    private String city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    private Long previewImageId;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    private LocalDateTime dateOfCreated;

    /**
     * Инициализация даты создания перед сохранением в БД.
     */
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    /**
     * Метод для добавления изображения к товару.
     * @param image Изображение, которое необходимо добавить.
     */
    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }

}
