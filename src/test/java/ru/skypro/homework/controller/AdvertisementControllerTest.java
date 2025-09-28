package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.model.dto.AdsDTO;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.service.AdvertisementService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertisementControllerTest {

    @Mock
    private AdvertisementService advertisementService;

    @InjectMocks
    private AdvertisementController advertisementController;

    @Test
    void getAllAds_ShouldReturnAdsDTO() {
        AdvertisementDTO ad1 = new AdvertisementDTO();
        AdvertisementDTO ad2 = new AdvertisementDTO();
        List<AdvertisementDTO> ads = List.of(ad1, ad2);
        AdsDTO adsDTO = new AdsDTO(2, ads);

        when(advertisementService.getAllAdvertisement()).thenReturn(adsDTO);

        ResponseEntity<AdsDTO> response = advertisementController.getAllAds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCount());
        verify(advertisementService).getAllAdvertisement();
    }

    @Test
    void createNewAds_ShouldReturnCreated() {
        AdvertisementDTO requestDTO = new AdvertisementDTO();
        AdvertisementDTO responseDTO = new AdvertisementDTO();

        when(advertisementService.createAds(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<AdvertisementDTO> response = advertisementController.createNewAds(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(advertisementService).createAds(requestDTO);
    }

    @Test
    void getAd_ShouldReturnExtendedAd() {
        Long adId = 1L;
        ExtendedAd extendedAd = new ExtendedAd();

        when(advertisementService.getExtendedAt(adId)).thenReturn(extendedAd);

        ResponseEntity<ExtendedAd> response = advertisementController.getAd(adId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(advertisementService).getExtendedAt(adId);
    }

    @Test
    void deleteAds_WithValidId() {
        Long adId = 1L;

        doNothing().when(advertisementService).deleteAdvertisement(adId);

        ResponseEntity<Void> response = advertisementController.deleteAds(adId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(advertisementService).deleteAdvertisement(adId);
    }

    @Test
    void getAllUsersAds_ShouldReturnUserAds() {
        AdvertisementDTO ad1 = new AdvertisementDTO();
        AdvertisementDTO ad2 = new AdvertisementDTO();
        List<AdvertisementDTO> ads = List.of(ad1, ad2);
        AdsDTO adsDTO = new AdsDTO(2, ads);

        when(advertisementService.getUserAllAdvertisement()).thenReturn(adsDTO);

        ResponseEntity<AdsDTO> response = advertisementController.getAllUsersAds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCount());
        verify(advertisementService).getUserAllAdvertisement();
    }
}
