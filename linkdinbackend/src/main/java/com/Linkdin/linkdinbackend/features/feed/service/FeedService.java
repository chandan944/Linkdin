package com.Linkdin.linkdinbackend.features.feed.service;

import com.Linkdin.linkdinbackend.features.authentication.model.AuthenticationUser;
import com.Linkdin.linkdinbackend.features.authentication.repository.AuthenticationUserRepository;
import com.Linkdin.linkdinbackend.features.feed.dto.PostDto;
import com.Linkdin.linkdinbackend.features.feed.model.Comment;
import com.Linkdin.linkdinbackend.features.feed.model.Post;
import com.Linkdin.linkdinbackend.features.feed.repository.CommentRepository;
import com.Linkdin.linkdinbackend.features.feed.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 🚀 This is a "service class" – like the brain 🧠 that handles logic for posts & comments
// 🎩 It's marked with @Service so Spring Boot knows it should manage this class like a superhero 🦸
@Service
public class FeedService {

    // 🧰 These are the tools we use to talk to the database (like talking to a magic notebook 📔)
    private final PostRepository postRepository;
    private final AuthenticationUserRepository userRepository;
    private final CommentRepository commentRepository;

    // 🛠️ Constructor: when FeedService is created, these 3 tools are given to it 🧳
    public FeedService(PostRepository postRepository, AuthenticationUserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 📝 1. Create a new post
    // Imagine a user wants to post "Hello World!" 🗨️ with a picture 📸
    public Post createPost(PostDto postDto, Long authorId) {
        // 🎯 Find the user who's making the post using their ID
        AuthenticationUser author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 📦 Create a new Post object with the post text and author
        Post post = new Post(postDto.getContent(), author);

        // 📸 Set the picture if there's one
        post.setPicture(postDto.getPicture());

        // ❤️ Set an empty like list (because no one has liked it yet)
        post.setLikes(new HashSet<>());

        // 💾 Save the post to the database and return it!
        return postRepository.save(post);
    }

    // 🔍 2. Get a single post using its ID
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    // ✏️ 3. Edit an existing post – like changing your caption from “Good Morning” to “Good Night” 😴
    public Post editPost(Long postId, Long userId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        AuthenticationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // ❌ If someone else tries to edit your post, stop them! 🛑
        if (!post.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the post");
        }

        // ✏️ Update content & picture
        post.setContent(postDto.getContent());
        post.setPicture(postDto.getPicture());

        return postRepository.save(post);
    }

    // 🗑️ 4. Delete a post
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        AuthenticationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // ❌ Only the author can delete it – protect your work! ⚔️
        if (!post.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the post");
        }

        postRepository.delete(post);
    }

    // ❤️ 5. Like or unlike a post (toggle)
    public Post likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        AuthenticationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 🔁 If user already liked the post, remove the like (unlike). Otherwise, add the like
        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
        }

        return postRepository.save(post);
    }

    // 💬 6. Add a comment to a post
    public Comment addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        AuthenticationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 🛠️ Create a new comment and save it
        Comment comment = new Comment(post, user, content);
        return commentRepository.save(comment);
    }

    // ✍️ 7. Edit a comment – only if it's yours! 🧑‍🎤
    public Comment editComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        AuthenticationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Only author can edit their comment ✏️
        if (!comment.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the comment");
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    // 🗑️ 8. Delete a comment
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        AuthenticationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Only author can delete their comment ✂️
        if (!comment.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the comment");
        }

        commentRepository.delete(comment);
    }

    // 👀 9. Get all posts made by a specific user (like your profile timeline 🧾)
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByAuthorId(userId);
    }

    // 📰 10. Get everyone’s posts except the currently logged-in user (feed view)
    public List<Post> getFeedPosts(Long authenticatedUserId) {
        return postRepository.findByAuthorIdNotOrderByCreationDateDesc(authenticatedUserId);
    }

    // 🌍 11. Get ALL posts (maybe for admin or explore page)
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }



    public Set<AuthenticationUser> getPostLikes(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return post.getLikes();
    }

    public List<Comment> getPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("Post not Found!") );
        return post.getComments();
    }
}

