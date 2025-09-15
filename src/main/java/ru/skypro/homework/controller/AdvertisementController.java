package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.AdvertisementDTO;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.awt.*;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdvertisementController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ads")
    public AdsDTO getAllAds() {
        return new AdsDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/ads")
    public AdvertisementDTO createNewAds(@RequestBody int author, String image,
                                         int pk, int price, String title) {
        return new AdvertisementDTO(author, image, pk, price, title);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ads/{id}")
    public ExtendedAd getAd(@PathVariable int id) {
        return new ExtendedAd();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/ads/{id}")
    public void deleteAds(@PathVariable int id) {
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/ads/{id}")
    public AdvertisementDTO editAds(@PathVariable int id,
                                    @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new AdvertisementDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ads/me")
    public AdsDTO getAllUsersAds() {
        return new AdsDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/ads/{id}/image")
    public Image editAdsImage(@PathVariable int id) {
        return null;
    }
}

