package ru.skypro.homework.dto;

public class CommentDTO {

    private int author;
    private String authorImage;
    private String authorFirsName;
    private int createdAt;
    private int pk;
    private String text;

    public CommentDTO(int author,
                      String authorImage,
                      String authorFirsName,
                      int createdAt,
                      int pk,
                      String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirsName = authorFirsName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }

    public CommentDTO() {
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorFirsName() {
        return authorFirsName;
    }

    public void setAuthorFirsName(String authorFirsName) {
        this.authorFirsName = authorFirsName;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
