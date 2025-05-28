package co.edu.uniquindio.theknowledgebay.core.service;

import co.edu.uniquindio.theknowledgebay.api.dto.*; // Import all DTOs from the correct package
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StudyGroupService {

    private final Map<String, StudyGroupDTO> studyGroups = new ConcurrentHashMap<>();
    private final Map<String, List<PostDTO>> groupPosts = new ConcurrentHashMap<>();
    private final AtomicLong groupCounter = new AtomicLong();
    private final AtomicLong postCounter = new AtomicLong();
    private final AtomicLong commentCounter = new AtomicLong();

    public StudyGroupService() {
        // NO LONGER INITIALIZING WITH HARDCODED DATA
        // The maps studyGroups and groupPosts will start empty.
    }

    public List<StudyGroupDTO> getAllGroups() {
        return new ArrayList<>(studyGroups.values());
    }

    public Optional<StudyGroupDTO> getGroupById(String groupId) {
        return Optional.ofNullable(studyGroups.get(groupId));
    }

    public List<PostDTO> getPostsByGroupId(String groupId, int page, int size) {
        List<PostDTO> posts = groupPosts.getOrDefault(groupId, Collections.emptyList());
        // Simple pagination, in a real app this would be more robust
        int start = page * size;
        if (start >= posts.size()) {
            return Collections.emptyList();
        }
        int end = Math.min(start + size, posts.size());
        return posts.subList(start, end);
    }

    public Optional<PostDTO> getPostById(String groupId, String postId) {
        return groupPosts.getOrDefault(groupId, Collections.emptyList())
            .stream()
            .filter(p -> p.getId().equals(postId))
            .findFirst();
    }

    public PostDTO likePost(String groupId, String postId, String userId) {
        PostDTO post = getPostById(groupId, postId)
            .orElseThrow(() -> new NoSuchElementException("Post not found: " + postId));
        
        // Basic like toggle, in a real app, track users who liked
        if (post.isLikedByMe()) { // Assuming likedByMe is a simple toggle for demo
            post.setLikes(post.getLikes() - 1);
            post.setLikedByMe(false);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.setLikedByMe(true);
        }
        return post;
    }

    public CommentDTO addCommentToPost(String groupId, String postId, String authorId, String authorName, String text) {
        PostDTO post = getPostById(groupId, postId)
            .orElseThrow(() -> new NoSuchElementException("Post not found: " + postId));
        
        UserSummary author = new UserSummary(authorId, authorName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy"));
        CommentDTO newComment = new CommentDTO("c" + commentCounter.incrementAndGet(), author, text, timestamp);
        
        post.getComments().add(newComment);
        return newComment;
    }
    
    // Placeholder for creating a new post - not fully implemented as per student restrictions
    // In a real scenario with permissions, this would be more fleshed out.
    public PostDTO createPost(String groupId, PostDTO postRequest) {
        if (!studyGroups.containsKey(groupId)) {
            throw new NoSuchElementException("Group not found: " + groupId);
        }
        String postId = "post" + postCounter.incrementAndGet();
        postRequest.setId(postId);
        postRequest.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")));
        // Ensure comments list is initialized for new posts
        if (postRequest.getComments() == null) {
            postRequest.setComments(new ArrayList<>());
        }
        groupPosts.computeIfAbsent(groupId, k -> new ArrayList<>()).add(0, postRequest); // Add to beginning
        return postRequest;
    }

    // Placeholder for creating a new group - not fully implemented as per student restrictions
    public StudyGroupDTO createStudyGroup(StudyGroupDTO groupRequest) {
        String groupId = "group" + groupCounter.incrementAndGet();
        groupRequest.setId(groupId);
        studyGroups.put(groupId, groupRequest);
        groupPosts.putIfAbsent(groupId, new ArrayList<>()); // Ensure a post list is ready for the new group
        return groupRequest;
    }
} 