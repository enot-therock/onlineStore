package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.excepption.ForbiddenException;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.mapper.AdvertisementMapper;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.Advertisement;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.AdvertisementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UsersService usersService;
    private final AdvertisementMapper advertisementMapper;

    public AdvertisementService(AdvertisementRepository advertisementRepository,
                                UsersService usersService,
                                AdvertisementMapper advertisementMapper) {
        this.advertisementRepository = advertisementRepository;
        this.usersService = usersService;
        this.advertisementMapper = advertisementMapper;
    }

    /**
     * метод создания нового объявления
     * @param advertisementDTO - DTO с исходными данными
     * @return - возвращаем DTO с информацией об объявлении
     */

    public AdvertisementDTO createAds(AdvertisementDTO advertisementDTO) {
        Users currentUser = usersService.getCurrentUser();

        Advertisement advertisement = advertisementMapper.ads(advertisementDTO);
        advertisement.setUser(currentUser);

        Advertisement createAdvertisement = advertisementRepository.save(advertisement);

        return advertisementMapper.adsToEntity(createAdvertisement);
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
        return advertisementMapper.adsToEntity(updateAdvertisement);
    }

    /**
     * метод для получения информации об конкретном объявлении
     * @param id - ID интересующего объявления
     * @return - возвращаем DTO с полной информацией об объявлении
     */

    public ExtendedAd getExtendedAt(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertisement is not found " + id));
        return advertisementMapper.extendedAdResponse(advertisement);
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
        List<AdvertisementDTO> advertisementDTOS = advertisements.stream()
                .map(advertisementMapper::adsToEntity)
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
        List<AdvertisementDTO> advertisementDTOS = advertisements.stream()
                .map(advertisementMapper::adsToEntity)
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
}
