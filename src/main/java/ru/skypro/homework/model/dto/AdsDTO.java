package ru.skypro.homework.model.dto;

import java.util.List;

public class AdsDTO {

    private int count;
    private List<AdvertisementDTO> results;

    public AdsDTO(int count, List<AdvertisementDTO> results) {
        this.count = count;
        this.results = results;
    }

    public AdsDTO() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AdvertisementDTO> getResult() {
        return results;
    }

    public void setResult(List<AdvertisementDTO> results) {
        this.results = results;
    }
}
