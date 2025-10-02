package ru.skypro.homework.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.skypro.homework.model.dto.Role;

import javax.persistence.*;
import java.util.List;

import static ru.skypro.homework.utils.PatternUtils.*;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 32, message = "Username must be between 4 and 32 symbol")
    @Column(name = "username", length = 32, nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 symbol")
    @Column(name = "password")
    private String password;

    @Size(min = 2, max = 16, message = "First name must be between 2 and 16 symbol")
    @Column(name = "firstName")
    private String firstName;

    @Size(min = 2, max = 16, message = "Last name must be between 2 and 16 symbol")
    @Column(name = "lastName")
    private String lastName;

    @NotBlank(message = "The user role must be selected")
    @Column(name = "role")
    private Role role;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = PATTERN_PHONE,
            message = "Phone number must have the appearance:" + PATTERN_NUMBER_PHONE)
    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Advertisement> advertisement;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comment;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Image image;

    @Column(name = "enabled")
    private boolean enabled = true;

    public Users(String username,
                 String password,
                 String firstName,
                 String lastName,
                 Role role,
                 String phone,
                 List<Advertisement> advertisement,
                 List<Comment> comment,
                 Image image) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phone = phone;
        this.advertisement = advertisement;
        this.comment = comment;
        this.image = image;
    }

    public Users(String username,
                 String password,
                 String firstName,
                 String lastName,
                 Role role,
                 String phone,
                 List<Advertisement> advertisement,
                 List<Comment> comment,
                 Image image,
                 boolean enabled) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phone = phone;
        this.advertisement = advertisement;
        this.comment = comment;
        this.image = image;
        this.enabled = enabled;
    }

    public Users() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Image getImage() {return image;}

    public void setImage(Image image) {this.image = image;}

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Advertisement> getAdvertisement() {
        return advertisement;
    }
    public void setAdvertisement(List<Advertisement> advertisement) {
        this.advertisement = advertisement;
    }

    public List<Comment> getComment() {
        return comment;
    }
    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}
}
