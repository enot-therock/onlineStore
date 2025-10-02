package ru.skypro.homework.model.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createdAt")
    private Instant createdAt;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    public Comment(String text, Users user, Advertisement advertisement) {
        this.text = text;
        this.user = user;
        this.advertisement = advertisement;
    }

    public Comment() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
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
