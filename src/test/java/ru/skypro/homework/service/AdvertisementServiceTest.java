package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.excepption.ForbiddenException;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.mapper.AdvertisementMapper;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.Advertisement;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.AdvertisementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvertisementServiceTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private AdvertisementMapper advertisementMapper;

    @InjectMocks
    private AdvertisementService advertisementService;

    private Users createTestUser(Long id, String username, Role role) {
        Users user = new Users();
        user.setId(id);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    private Advertisement createTestAd(Long id, Users user) {
        Advertisement ad = new Advertisement();
        ad.setId(id);
        ad.setTitle("Test Ad");
        ad.setPrice(1000);
        ad.setDescription("Test Description");
        ad.setUser(user);
        return ad;
    }

    private AdvertisementDTO createTestAdDTO() {
        AdvertisementDTO dto = new AdvertisementDTO();
        dto.setTitle("Test Ad");
        dto.setPrice(1000);
        return dto;
    }

    private CreateOrUpdateAd createTestUpdateAd() {
        CreateOrUpdateAd updateAd = new CreateOrUpdateAd();
        updateAd.setTitle("Updated Ad");
        updateAd.setPrice(2000);
        updateAd.setDescription("Updated Description");
        return updateAd;
    }

    @Test
    void createAds_WithValidData() {
        Users currentUser = createTestUser(1L, "test@example.com", Role.USER);
        AdvertisementDTO requestDTO = createTestAdDTO();
        Advertisement ad = createTestAd(1L, currentUser);
        AdvertisementDTO responseDTO = createTestAdDTO();

        when(usersService.getCurrentUser()).thenReturn(currentUser);
        when(advertisementMapper.ads(requestDTO)).thenReturn(ad);
        when(advertisementRepository.save(any(Advertisement.class))).thenReturn(ad);
        when(advertisementMapper.adsToEntity(ad)).thenReturn(responseDTO);

        AdvertisementDTO result = advertisementService.createAds(requestDTO);

        assertNotNull(result);
        verify(usersService).getCurrentUser();
        verify(advertisementMapper).ads(requestDTO);
        verify(advertisementRepository).save(ad);
        verify(advertisementMapper).adsToEntity(ad);
        assertEquals(currentUser, ad.getUser());
    }

    @Test
    void updateAds_WhenUserIsOwner() {
        Long adId = 1L;
        Users owner = createTestUser(1L, "owner@example.com", Role.USER);
        Advertisement ad = createTestAd(adId, owner);
        CreateOrUpdateAd updateRequest = createTestUpdateAd();
        AdvertisementDTO responseDTO = createTestAdDTO();
        responseDTO.setTitle("Updated Ad");

        when(usersService.getCurrentUser()).thenReturn(owner);
        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(advertisementRepository.save(any(Advertisement.class))).thenReturn(ad);
        when(advertisementMapper.adsToEntity(ad)).thenReturn(responseDTO);

        AdvertisementDTO result = advertisementService.updateAds(adId, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Ad", result.getTitle());
        assertEquals("Updated Ad", ad.getTitle());
        assertEquals(2000, ad.getPrice());
        assertEquals("Updated Description", ad.getDescription());
        verify(advertisementRepository).save(ad);
    }

    @Test
    void updateAds_WhenUserIsNotOwner() {
        Long adId = 1L;
        Users owner = createTestUser(1L, "owner@example.com", Role.USER);
        Users otherUser = createTestUser(2L, "other@example.com", Role.USER);
        Advertisement ad = createTestAd(adId, owner);
        CreateOrUpdateAd updateRequest = createTestUpdateAd();

        when(usersService.getCurrentUser()).thenReturn(otherUser);
        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));

        assertThrows(ForbiddenException.class, () -> advertisementService.updateAds(adId, updateRequest));
        verify(advertisementRepository, never()).save(any(Advertisement.class));
    }

    @Test
    void getExtendedAd_WithExistingAd() {
        Long adId = 1L;
        Users user = createTestUser(1L, "test@example.com", Role.USER);
        Advertisement ad = createTestAd(adId, user);
        ExtendedAd extendedAd = new ExtendedAd();

        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(advertisementMapper.extendedAdResponse(ad)).thenReturn(extendedAd);

        ExtendedAd result = advertisementService.getExtendedAt(adId);

        assertNotNull(result);
        verify(advertisementRepository).findById(adId);
        verify(advertisementMapper).extendedAdResponse(ad);
    }

    @Test
    void getExtendedAd_WithNonExistentAd() {
        Long adId = 999L;

        when(advertisementRepository.findById(adId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                advertisementService.getExtendedAt(adId)
        );
    }

    @Test
    void deleteAdvertisement_WhenUserIsOwner() {
        Long adId = 1L;
        Users owner = createTestUser(1L, "owner@example.com", Role.USER);
        Advertisement ad = createTestAd(adId, owner);

        when(usersService.getCurrentUser()).thenReturn(owner);
        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));

        advertisementService.deleteAdvertisement(adId);

        verify(advertisementRepository).delete(ad);
    }

    @Test
    void deleteAdvertisement_WhenUserIsNotOwner() {
        Long adId = 1L;
        Users owner = createTestUser(1L, "owner@example.com", Role.USER);
        Users otherUser = createTestUser(2L, "other@example.com", Role.USER);
        Advertisement ad = createTestAd(adId, owner);

        when(usersService.getCurrentUser()).thenReturn(otherUser);
        when(advertisementRepository.findById(adId)).thenReturn(Optional.of(ad));

        assertThrows(ForbiddenException.class, () -> advertisementService.deleteAdvertisement(adId));
        verify(advertisementRepository, never()).delete(any(Advertisement.class));
    }

    @Test
    void getAllAdvertisement_ShouldReturnAllAds() {
        Users user1 = createTestUser(1L, "user1@example.com", Role.USER);
        Users user2 = createTestUser(2L, "user2@example.com", Role.USER);
        Advertisement ad1 = createTestAd(1L, user1);
        Advertisement ad2 = createTestAd(2L, user2);
        List<Advertisement> ads = List.of(ad1, ad2);

        AdvertisementDTO dto1 = createTestAdDTO();
        AdvertisementDTO dto2 = createTestAdDTO();

        when(advertisementRepository.findAll()).thenReturn(ads);
        when(advertisementMapper.adsToEntity(ad1)).thenReturn(dto1);
        when(advertisementMapper.adsToEntity(ad2)).thenReturn(dto2);

        AdsDTO result = advertisementService.getAllAdvertisement();

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResult().size());
        verify(advertisementRepository).findAll();
        verify(advertisementMapper, times(2)).adsToEntity(any(Advertisement.class));
    }

    @Test
    void getUserAllAdvertisement_ShouldReturnUserAds() {
        Users currentUser = createTestUser(1L, "test@example.com", Role.USER);
        Advertisement ad1 = createTestAd(1L, currentUser);
        Advertisement ad2 = createTestAd(2L, currentUser);
        List<Advertisement> userAds = List.of(ad1, ad2);

        AdvertisementDTO dto1 = createTestAdDTO();
        AdvertisementDTO dto2 = createTestAdDTO();

        when(usersService.getCurrentUser()).thenReturn(currentUser);
        when(advertisementRepository.findByUser(currentUser)).thenReturn(userAds);
        when(advertisementMapper.adsToEntity(ad1)).thenReturn(dto1);
        when(advertisementMapper.adsToEntity(ad2)).thenReturn(dto2);

        AdsDTO result = advertisementService.getUserAllAdvertisement();

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResult().size());
        verify(usersService).getCurrentUser();
        verify(advertisementRepository).findByUser(currentUser);
        verify(advertisementMapper, times(2)).adsToEntity(any(Advertisement.class));
    }
}
