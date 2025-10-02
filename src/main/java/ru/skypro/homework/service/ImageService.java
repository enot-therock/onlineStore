package ru.skypro.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.model.entity.Advertisement;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.AdvertisementRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UsersRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final UsersRepository usersRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UsersService usersService;

    public ImageService(ImageRepository imageRepository,
                        UsersRepository usersRepository,
                        AdvertisementRepository advertisementRepository,
                        UsersService usersService) {
        this.imageRepository = imageRepository;
        this.usersRepository = usersRepository;
        this.advertisementRepository = advertisementRepository;
        this.usersService = usersService;
    }

    @Value("${file.upload.directory}")
    private String uploadDir;

    public String savedUserImage(Users user, MultipartFile imageFile) throws IOException {
        if (user.getImage() != null) {
            deleteImageFromFileSystem(user.getImage().getFilePath());
            imageRepository.delete(user.getImage());
        }

        Image image = saveImageToFileSystem(imageFile, user, null);
        user.setImage(image);
        usersRepository.save(user);

        return getImageUrlUsers(image.getId());
    }

    public String savedAdvertisementImage(Advertisement advertisement, MultipartFile imageFile) throws IOException {
        Users currentUser = usersService.getCurrentUser();

        if (advertisement.getImage() != null) {
            deleteImageFromFileSystem(advertisement.getImage().getFilePath());
            imageRepository.delete(advertisement.getImage());
        }

        Image image = saveImageToFileSystem(imageFile, null, advertisement);
        advertisement.setImage(image);

        advertisementRepository.save(advertisement);

        return getImageUrlAds(image.getId());
    }

    /**
     * доп методы для сохранения / удаления файла изображения в системе
     * @throws IOException - input / output exception
     */

    public Image saveImageToFileSystem(MultipartFile file, Users user, Advertisement advertisement) throws IOException{
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String fileName = UUID.randomUUID() + "." + fileExtension;
        String filePath = uploadPath.resolve(fileName).toString();

        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        Image image = new Image();
        image.setFileName(originalFileName);
        image.setFilePath(filePath);
        image.setUser(user);
        image.setAdvertisement(advertisement);

        image.setMediaType(file.getContentType());
        image.setFileSize(file.getSize());

        Image result = imageRepository.save(image);
        advertisement.setImage(result);

        return result;
    }

    private void deleteImageFromFileSystem(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            try {
                Files.delete(path);
                System.out.println("File to delete successfully: " + filePath);
            } catch (IOException e) {
                System.err.println("Filed to delete file: " + filePath);
                throw e;
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    /**
     * доп метод для коррекции имени файла
     */

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "jpg";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * доп методы для преобразования Image в строку адреса для DTO
     */

    public String getImageUrlUsers(Long imageId) {
        return "/users/me/image";
    }

    public String getImageUrlAds(Long imageId) {
        return "/ads/image/" + imageId;
    }

    public String getUpdateImageUrlAd(Long adId) {
        return "/ads/" + adId + "/image";
    }

    /**
     * метод получения изображения
     */

    public byte[] getImageData(Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
        return Files.readAllBytes(Paths.get(image.getFilePath()));
    }
}
