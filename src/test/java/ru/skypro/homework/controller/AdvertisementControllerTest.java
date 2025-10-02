package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDTO;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.service.AdvertisementService;

import java.io.IOException;
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
    void createNewAds_ShouldReturnCreated() throws IOException {
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("Test Advertisement");
        properties.setPrice(1000);
        properties.setDescription("Test Description");

        MultipartFile image = mock(MultipartFile.class);
        AdvertisementDTO expectedDto = new AdvertisementDTO(1, "test-image.jpg", 1, 1000, "Test Advertisement");

        when(advertisementService.addAd(properties, image)).thenReturn(expectedDto);

        ResponseEntity<AdvertisementDTO> response = advertisementController.addAd(properties, image);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedDto, response.getBody());
        assertEquals(1, response.getBody().getPk());
        assertEquals("Test Advertisement", response.getBody().getTitle());
        assertEquals(1000, response.getBody().getPrice());
        verify(advertisementService).addAd(properties, image);
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
