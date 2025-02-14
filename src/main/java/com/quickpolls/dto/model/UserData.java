package com.quickpolls.dto.model;

public class UserData {
    private Long id;
    private String nickname;
    private String image;

    public UserData(Long id, String nickname, String image) {
        this.id = id;
        this.nickname = nickname;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
