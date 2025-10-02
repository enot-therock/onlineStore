package ru.skypro.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.excepption.ForbiddenException;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.mapper.AdvertisementMapper;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.Advertisement;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.AdvertisementRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UsersService usersService;
    private final AdvertisementMapper advertisementMapper;
    private final ImageService imageService;

    public AdvertisementService(AdvertisementRepository advertisementRepository,
                                UsersService usersService,
                                AdvertisementMapper advertisementMapper,
                                ImageService imageService) {
        this.advertisementRepository = advertisementRepository;
        this.usersService = usersService;
        this.advertisementMapper = advertisementMapper;
        this.imageService = imageService;
    }

    /**
     * метод создания нового объявления
     * advertisementDTO - DTO с исходными данными
     * @return - возвращаем DTO с информацией об объявлении
     */

    public AdvertisementDTO addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file) throws IOException {
        Users currentUser = usersService.getCurrentUser();

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(createOrUpdateAd.getTitle());
        advertisement.setPrice(createOrUpdateAd.getPrice());
        advertisement.setDescription(createOrUpdateAd.getDescription());
        advertisement.setUser(currentUser);

        Advertisement createAdvertisement = advertisementRepository.save(advertisement);

        imageService.savedAdvertisementImage(createAdvertisement, file);

        Advertisement updatedAdvertisement = advertisementRepository.findById(createAdvertisement.getId())
                .orElseThrow(() -> new NotFoundException("Advertisement not found after creation"));

        return advertisementMapper.toAdvertisementDTO(updatedAdvertisement);
    }

    /**
     * метод для обновления данных (редактирования) объявления
     * @param advertisementId - ID нужного объявления
     * @param createOrUpdateAd - DTO обновляемой информации
     * @return - DTO обновленного объявления
     */

    public AdvertisementDTO updateAds(Long advertisementId, CreateOrUpdateAd createOrUpdateAd) {
        Users currentUser = usersService.getCurrentUser();

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new NotFoundException("Advertisement is not found " + advertisementId));


        if (!isOwnerOrAdmin(advertisement, currentUser)) {
            throw new ForbiddenException("You don`t have edit advertisement");
        }

        advertisement.setTitle(createOrUpdateAd.getTitle());
        advertisement.setPrice(createOrUpdateAd.getPrice());
        advertisement.setDescription(createOrUpdateAd.getDescription());

        Advertisement updateAdvertisement = advertisementRepository.save(advertisement);
        return advertisementMapper.toAdvertisementDTO(updateAdvertisement);
    }

    /**
     * метод для получения информации об конкретном объявлении
     * @param id - ID интересующего объявления
     * @return - возвращаем DTO с полной информацией об объявлении
     */

    public ExtendedAd getExtendedAt(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertisement is not found " + id));
        return advertisementMapper.toExtendedAd(advertisement);
    }

    /**
     * метод для удаления объявления
     * @param id - ID нужного объявления
     */

    public void deleteAdvertisement(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertisement is not found " + id));

        Users currentUser = usersService.getCurrentUser();

        if (!isOwnerOrAdmin(advertisement, currentUser)) {
            throw new ForbiddenException("You don`t have delete advertisement");
        }

        advertisementRepository.delete(advertisement);
    }

    /**
     * метод для получения всех объявлений (работает для всех пользователей)
     * @return - возвращает DTO списком все доступных объявлений
     */

    public AdsDTO getAllAdvertisement() {
        List<Advertisement> advertisements = advertisementRepository.findAll();

        if (advertisements == null || advertisements.isEmpty()) {
            return new AdsDTO(0, Collections.emptyList());
        }

        List<AdvertisementDTO> advertisementDTOS = advertisements.stream()
                .map(advertisementMapper::toAdvertisementDTO)
                .collect(Collectors.toList());

        return new AdsDTO(advertisementDTOS.size(), advertisementDTOS);
    }

    /**
     * метод для получения всех объявлений авторизованного пользователя
     * @return - возвращает DTO списком все доступных объявлений
     */

    public AdsDTO getUserAllAdvertisement() {
        Users currentUser = usersService.getCurrentUser();

        List<Advertisement> advertisements = advertisementRepository.findByUser(currentUser);

        if (advertisements == null || advertisements.isEmpty()) {
            return new AdsDTO(0, Collections.emptyList());
        }

        List<AdvertisementDTO> advertisementDTOS = advertisements.stream()
                .map(advertisementMapper::toAdvertisementDTO)
                .collect(Collectors.toList());

        return new AdsDTO(advertisementDTOS.size(), advertisementDTOS);
    }


    /**
     * доп метод ля проверки прав доступа
     *      - isOwner права авторизованного пользователя
     *      - isAdmin - права администратора
     */

    private boolean isOwnerOrAdmin(Advertisement advertisement, Users currentUser) {
        boolean isOwner = advertisement.getUser().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        return isOwner || isAdmin;
    }

    /**
     * метод для обновления изображения у объявлений
     * @param adId - ID объявления
     */

    public byte[] updateAdvertisementImage(Integer adId, MultipartFile image) throws IOException {
        Advertisement advertisement = advertisementRepository.findById(adId.longValue())
                .orElseThrow(() -> new NotFoundException("Advertisement not found with id: " + adId));

        Users currentUser = usersService.getCurrentUser();
        if (!advertisement.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can only update your own advertisements");
        }

        imageService.savedAdvertisementImage(advertisement, image);

        return image.getBytes();
    }
}
