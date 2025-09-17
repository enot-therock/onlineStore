package ru.skypro.homework.dto;

import java.util.List;

public class Comments {

    private int count;
    private List<CommentDTO> result;

    public Comments(int count, List<CommentDTO> result) {
        this.count = count;
        this.result = result;
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
        return result;
    }

    public void setResult(List<CommentDTO> result) {
        this.result = result;
    }
}
