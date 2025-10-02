package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.model.entity.Advertisement;
import ru.skypro.homework.model.entity.Image;

@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapImageToUrl")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    AdvertisementDTO toAdvertisementDTO(Advertisement advertisement);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "user.username", target = "email")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapImageToUrl")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    ExtendedAd toExtendedAd(Advertisement advertisement);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    CreateOrUpdateAd toCreateOrUpdateAd(Advertisement advertisement);

    /**
     * Маппим обратно и обновляем данные
     */

    @Mapping(source = "title", target = "title")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    Advertisement toAdvertisement(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "user.id")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapUrlToImage")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    Advertisement toAdvertisementEntity(AdvertisementDTO advertisementDTO);

    @Named("mapImageToUrl")
    default String mapImageToUrl(Image image) {
        if (image == null) {
            return null;
        }
        return "ads/image/" + image.getId();
    }

    @Named("mapUrlToImage")
    default Image mapUrlToImage(String imagePath) {
        if (imagePath == null) return null;
        Image image = new Image();
        image.setFilePath(imagePath);
        return image;
    }
}
