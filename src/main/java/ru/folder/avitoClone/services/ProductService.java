package ru.folder.avitoClone.services;

import ru.folder.avitoClone.repositories.ProductRepository;
import ru.folder.avitoClone.repositories.UserRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import ru.folder.avitoClone.models.Product;
import ru.folder.avitoClone.models.Image;
import ru.folder.avitoClone.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.io.IOException;
import java.util.List;

/**
 * Сервис для работы с товарами и изображениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Получить список товаров по указанному заголовку или все товары, если заголовок не указан.
     *
     * @param title Заголовок для поиска товаров.
     * @return Список товаров.
     */
    public List<Product> listProducts(String title) {
        if (title != null) return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    /**
     * Сохранить новый товар вместе с изображениями.
     *
     * @param principal Текущий пользователь.
     * @param product   Товар для сохранения.
     * @param file1     Первое изображение.
     * @param file2     Второе изображение.
     * @param file3     Третье изображение.
     * @throws IOException Возникает при ошибке работы с изображениями.
     */
    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(User user, Long id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
