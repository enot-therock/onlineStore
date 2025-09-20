package ru.skypro.homework.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private int price;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private Users user;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private List<Comment> comment;

    public Advertisement(String description,
                         String image,
                         String title,
                         int price,
                         Users user,
                         List<Comment> comment) {
        this.description = description;
        this.image = image;
        this.title = title;
        this.price = price;
        this.user = user;
        this.comment = comment;
    }

    public Advertisement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }
}
