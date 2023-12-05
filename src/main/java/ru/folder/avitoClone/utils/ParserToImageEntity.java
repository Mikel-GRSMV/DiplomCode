package ru.folder.avitoClone.utils;

import org.springframework.web.multipart.MultipartFile;
import ru.folder.avitoClone.models.Image;

import java.io.IOException;

public class ParserToImageEntity {

    public static Image toImageEntity(MultipartFile file) throws IOException {

        Image image = new Image();

        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());

        return image;

    }

}
