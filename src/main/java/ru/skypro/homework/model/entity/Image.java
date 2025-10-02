package ru.skypro.homework.model.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_path")
    private String filePath;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @OneToOne
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public Image(String fileName, String mediaType, Long fileSize, String filePath, Users user, Advertisement advertisement, Instant createdAt) {
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.user = user;
        this.advertisement = advertisement;
        this.createdAt = createdAt;
    }

    public Image() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}

    public String getFilePath() {return filePath;}
    public void setFilePath(String filePath) {this.filePath = filePath;}

    public Users getUser() {return user;}
    public void setUser(Users user) {this.user = user;}

    public Advertisement getAdvertisement() {return advertisement;}
    public void setAdvertisement(Advertisement advertisement) {this.advertisement = advertisement;}

    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
