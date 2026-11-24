package kr.co.private_box.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;

import java.util.Random;

@Entity
@Table(name = "private_boxes")
public class PrivateBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false,unique = true)
    private String privateUrl;

    @Column(length = 20)
    private String urlPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrivateBoxStatus privateBoxStatus;

    public PrivateBox() {
    }

    public PrivateBox(String originalUrl, String privateUrl, String urlPassword) {
        this.originalUrl = originalUrl;
        this.privateUrl = privateUrl;
        this.urlPassword = urlPassword;
        this.privateBoxStatus=PrivateBoxStatus.ACTIVE;
    }

    public Boolean isEqualsPassword(String urlPassword){
        return this.urlPassword.equals(urlPassword);
    }

    public void expirePrivateUrl(){
        this.privateBoxStatus = PrivateBoxStatus.EXPIRED;
    }

    public Boolean isExpired(){
        return this.privateBoxStatus.equals(PrivateBoxStatus.EXPIRED);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivateUrl() {
        return privateUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public PrivateBoxStatus getPrivateBoxStatus() {
        return privateBoxStatus;
    }

    public Long getId() {
        return id;
    }

    public String getUrlPassword() {
        return urlPassword;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setPrivateUrl(String privateUrl) {
        this.privateUrl = privateUrl;
    }

    public void setUrlPassword(String urlPassword) {
        this.urlPassword = urlPassword;
    }

    public void setPrivateBoxStatus(PrivateBoxStatus privateBoxStatus) {
        this.privateBoxStatus = privateBoxStatus;
    }
}