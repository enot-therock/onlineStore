package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.dto.AdsDTO;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.service.AdvertisementService;

import java.awt.*;

@Slf4j
@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    /**
     * эндпоинт для получения всех объявлений
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     */

    @GetMapping()
    public ResponseEntity<AdsDTO> getAllAds() {
        AdsDTO adsDTO = advertisementService.getAllAdvertisement();
        return ResponseEntity.ok(adsDTO);
    }

    /**
     * эндпоинт для добавления объявления
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     */

    @PostMapping
    public ResponseEntity<AdvertisementDTO> createNewAds(@RequestBody AdvertisementDTO advertisementDTO) {
        AdvertisementDTO createAds = advertisementService.createAds(advertisementDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createAds);
    }

    /**
     * эндпоинт для получения информации об объявлении
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 404 Not Found (нет информации)
     */

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Long id) {
        ExtendedAd extendedAd = advertisementService.getExtendedAt(id);
            return ResponseEntity.ok(extendedAd);
    }

    /**
     * эндпоинт для удаления объявления
     * возможные коды ответа:
     *      - 204 No Content (удален - пусто)
     *      - 401 Unauthorized (не авторизован)
     *      - 403 Forbidden (недостаточно прав доступа)
     *      - 404 Not Found (нет информации)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * эндпоинт для обновления информации об объявлении
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 403 Forbidden (недостаточно прав доступа)
     *      - 404 Not Found (нет информации)
     */

    @PatchMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> editAds(@PathVariable Long id,
                                                    @RequestBody CreateOrUpdateAd createOrUpdateAd) {

        AdvertisementDTO advertisementDTO = advertisementService.updateAds(id, createOrUpdateAd);
            return ResponseEntity.ok(advertisementDTO);
    }

    /**
     * эндпоинт для получения объявлений авторизованного пользователя
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     */

    @GetMapping("/me")
    public ResponseEntity<AdsDTO> getAllUsersAds() {
        AdsDTO adsDTO = advertisementService.getUserAllAdvertisement();
        return ResponseEntity.ok(adsDTO);
    }

    /**
     * эндпоинт для обновления картинки объявления
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 403 Forbidden (недостаточно прав доступа)
     *      - 404 Not Found (нет информации)
     */

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/image")
    public Image editAdsImage(@PathVariable int id) {
        return null;
    }
}

