package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.model.entity.Advertisement;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "id", target = "pk")
    AdvertisementDTO adsToEntity(Advertisement ads);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.firstName", target = "authorFirsName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "user.username", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    ExtendedAd extendedAdResponse(Advertisement adsId);

    CreateOrUpdateAd updateAds(Advertisement adsId);

    /**
     * Маппим обратно и обновляем данные
     */

    @Mapping(target = "id", expression = "java((long) advertisementDTO.getPk())")
    Advertisement ads(AdvertisementDTO advertisementDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    Advertisement adsUpdate(ExtendedAd extendedAd);
}
