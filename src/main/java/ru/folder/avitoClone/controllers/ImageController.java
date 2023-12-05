package ru.folder.avitoClone.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;

import ru.folder.avitoClone.repositories.ImageRepository;
import ru.folder.avitoClone.models.Image;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {

        Image image = imageRepository.findById(id).orElse(null);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));

    }
}
