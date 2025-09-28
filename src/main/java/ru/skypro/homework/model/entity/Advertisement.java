package ru.skypro.homework.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 8, max = 64, message = "Description must be between 8 and 64 symbol")
    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @NotBlank(message = "Title is required")
    @Size(min = 4, max = 32, message = "Title must be between 4 and 32 symbol")
    @Column(name = "title")
    private String title;

    @Size(max = 10000000, message = "Price must have maximum 10000000 symbol")
    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
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
