package ru.skypro.homework.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "text")
    private String text;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private Users user;

    @OneToOne
    @JoinColumn(name = "advertisementId", referencedColumnName = "id")
    private Advertisement advertisement;

    public Comment(LocalDateTime createdAt, String text, Users user, Advertisement advertisement) {
        this.createdAt = createdAt;
        this.text = text;
        this.user = user;
        this.advertisement = advertisement;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
