package ru.skypro.homework.dto;

import java.util.List;

public class AdsDTO {

    private int count;
    private List<AdvertisementDTO> result;

    public AdsDTO(int count, List<AdvertisementDTO> result) {
        this.count = count;
        this.result = result;
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
        return result;
    }

    public void setResult(List<AdvertisementDTO> result) {
        this.result = result;
    }
}
