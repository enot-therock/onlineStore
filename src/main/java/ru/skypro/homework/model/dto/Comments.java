package ru.skypro.homework.model.dto;

import java.util.List;

public class Comments {

    private int count;
    private List<CommentDTO> results;

    public Comments(int count, List<CommentDTO> results) {
        this.count = count;
        this.results = results;
    }

    public Comments() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CommentDTO> getResult() {
        return results;
    }

    public void setResult(List<CommentDTO> results) {
        this.results = results;
    }
}
