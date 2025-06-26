package com.Linkdin.linkdinbackend.features.feed.model;

import com.Linkdin.linkdinbackend.features.authentication.model.AuthenticationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// 🧱 This means the class becomes a table in the database 📦
// The table name will be: posts
@Entity(name = "posts")
public class Post {

    // 🆔 This is like the roll number of the post! 🎟️
    // It's unique for every post and gets created automatically (like 1, 2, 3…)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 💬 This is the actual message someone writes in a post
    // We can't allow it to be empty — a post with no message? Nope 🙅‍♂️
    @NotEmpty
    private String content;

    // 🖼️ Optional: A picture you might attach to your post
    private String picture;

    // 👤 This says: “Who wrote this post?”
    // Every post must have one author (user)
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthenticationUser author;

    // ❤️ Likes! When many users ❤️ a post, it stores their names here.
    // This is a special table that links posts with users
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "posts_likes", // table name in the DB 🍽️
            joinColumns = @JoinColumn(name = "post_id"), // connects to the post
            inverseJoinColumns = @JoinColumn(name = "user_id") // connects to the user who liked it
    )
    private Set<AuthenticationUser> likes;

    // 💬💬 These are the comments people leave on the post
    // Like: “Wow, congrats! 🎉” or “Great work!”
    // One post can have **many comments**
    // If a post is deleted, comments go with it 🗑️
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // ⏰ This will automatically store the time when the post was created
    // You don’t need to set it — Hibernate (Spring’s helper) does it for you 🧙‍♂️
    @CreationTimestamp
    private LocalDateTime creationDate;

    // 🛠️ This stores the last time the post was updated/edited (like when you fix a typo ✏️)
    private LocalDateTime updatedDate;

    // 👷‍♂️ Custom constructor to make a post with content and author
    public Post(String content, AuthenticationUser author) {
        this.content = content;
        this.author = author;
    }

    // 🛠️ Default constructor – required by JPA (don't worry, it just needs to be here)
    public Post() {
    }

    // ✏️ This method is called automatically before a post is updated
    // It updates the edited time so we know when it was last changed
    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now(); // now = current time ⏰
    }

    // 🔽 Below are the "getters" and "setters"
    // Imagine them as the way you ask and tell the post object things

    // 📌 What is the post ID?
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 💬 What’s the message?
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 👤 Who wrote it?
    public AuthenticationUser getAuthor() {
        return author;
    }

    public void setAuthor(AuthenticationUser author) {
        this.author = author;
    }

    // ❤️ Who liked it?
    public Set<AuthenticationUser> getLikes() {
        return likes;
    }

    public void setLikes(Set<AuthenticationUser> likes) {
        this.likes = likes;
    }

    // 🖼️ Picture stuff
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    // 💬💬 What are the comments?
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // 🕓 When was it made?
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    // 🛠️ When was it edited?
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}

