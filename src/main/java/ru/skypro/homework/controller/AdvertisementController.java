package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.dto.AdsDTO;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;

import java.awt.*;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdvertisementController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public AdsDTO getAllAds() {
        return new AdsDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public AdvertisementDTO createNewAds(@RequestParam int author,
                                         @RequestParam String image,
                                         @RequestParam int pk,
                                         @RequestParam int price,
                                         @RequestParam String title) {
        return new AdvertisementDTO(author, image, pk, price, title);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ExtendedAd getAd(@PathVariable int id) {
        return new ExtendedAd();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteAds(@PathVariable int id) {
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}")
    public AdvertisementDTO editAds(@PathVariable int id,
                                    @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new AdvertisementDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public AdsDTO getAllUsersAds() {
        return new AdsDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/image")
    public Image editAdsImage(@PathVariable int id) {
        return null;
    }
}

