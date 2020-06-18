package com.developer.instagram.Model;

public class User
{
    private String name;
    private String Bio;
    private String username;
    private String imageUrl;
    private String id;
    private String email;

    public User() {

    }

    public User(String name, String bio, String username, String imageUrl, String id, String email) {
        this.name = name;
        Bio = bio;
        this.username = username;
        this.imageUrl = imageUrl;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
