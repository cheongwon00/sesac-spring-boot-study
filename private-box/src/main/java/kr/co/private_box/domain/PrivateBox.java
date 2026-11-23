package kr.co.private_box.domain;

import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;

import java.util.Random;

public class PrivateBox {
    private Long id;
    private String originalUrl;
    private String privateUrl;

    @Size(min = 0, max = 20,message = "private url의 비밀번호의 길이는 0~20입니다.")
    private String urlPassword;
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