package com.developer.instagram.Model;

public class Post
{

    private String Description;
    private String Publisher;
    private String PostId;
    private String ImageUrl;

    public Post() {
    }

    public Post(String description, String publisher, String postId, String imageUrl) {
        Description = description;
        Publisher = publisher;
        PostId = postId;
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
