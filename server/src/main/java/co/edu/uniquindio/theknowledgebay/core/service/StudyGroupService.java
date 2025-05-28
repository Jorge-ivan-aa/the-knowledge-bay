package co.edu.uniquindio.theknowledgebay.core.service;

import co.edu.uniquindio.theknowledgebay.api.dto.*;
import co.edu.uniquindio.theknowledgebay.core.model.StudyGroup; // Model class
import co.edu.uniquindio.theknowledgebay.core.model.TheKnowledgeBay;
import co.edu.uniquindio.theknowledgebay.infrastructure.util.datastructures.lists.DoublyLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class StudyGroupService {

    private final TheKnowledgeBay theKnowledgeBay;

    // groupPosts will store posts for groups managed by TheKnowledgeBay or manually created ones.
    // The IDs will align if groups are consistently identified (e.g., by the string ID we implemented).
    private final Map<String, List<PostDTO>> groupPosts = new ConcurrentHashMap<>();
    private final AtomicLong postCounter = new AtomicLong(); // For posts within any group
    private final AtomicLong commentCounter = new AtomicLong(); // For comments within any post
    private final AtomicLong manualGroupCounter = new AtomicLong(); // For manually created groups, if any

    @Autowired
    public StudyGroupService(TheKnowledgeBay theKnowledgeBay) {
        this.theKnowledgeBay = theKnowledgeBay;
    }

    private StudyGroupDTO mapToDTO(StudyGroup groupModel) {
        if (groupModel == null) return null;
        return new StudyGroupDTO(
                groupModel.getId(),
                groupModel.getName(),
                groupModel.getTopic() != null ? groupModel.getTopic().getName() : "Unknown",
                groupModel.getMembers() != null ? groupModel.getMembers().getSize() : 0
        );
    }

    public List<StudyGroupDTO> getAllGroups() {
        DoublyLinkedList<StudyGroup> groupsFromCore = theKnowledgeBay.getStudyGroups();
        List<StudyGroupDTO> dtos = new ArrayList<>();
        if (groupsFromCore != null) {
            for (int i = 0; i < groupsFromCore.getSize(); i++) {
                dtos.add(mapToDTO(groupsFromCore.get(i)));
            }
        }
        // If manual groups were also supported and stored elsewhere in this service, merge here.
        // For now, only showing groups from TheKnowledgeBay.
        return dtos;
    }

    public Optional<StudyGroupDTO> getGroupById(String groupId) {
        DoublyLinkedList<StudyGroup> groupsFromCore = theKnowledgeBay.getStudyGroups();
        if (groupsFromCore != null) {
            for (int i = 0; i < groupsFromCore.getSize(); i++) {
                StudyGroup groupModel = groupsFromCore.get(i);
                if (groupModel.getId().equals(groupId)) {
                    return Optional.ofNullable(mapToDTO(groupModel));
                }
            }
        }
        return Optional.empty();
    }

    public List<PostDTO> getPostsByGroupId(String groupId, int page, int size) {
        // This part remains largely the same, as posts are associated by ID, 
        // whether the group is auto or manually created.
        List<PostDTO> posts = groupPosts.getOrDefault(groupId, Collections.emptyList());
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
            .orElseThrow(() -> new NoSuchElementException("Post not found: " + postId + " in group " + groupId));
        
        if (post.isLikedByMe()) { 
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
            .orElseThrow(() -> new NoSuchElementException("Post not found: " + postId + " in group " + groupId));
        
        UserSummary author = new UserSummary(authorId, authorName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy"));
        CommentDTO newComment = new CommentDTO("c" + commentCounter.incrementAndGet(), author, text, timestamp);
        
        // Ensure comments list is initialized
        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }
        post.getComments().add(newComment);
        return newComment;
    }
    
    // This method is now for MANUAL post creation by an authorized user (e.g. admin/moderator)
    // It assumes the groupId corresponds to a group that exists (either auto or manually created).
    public PostDTO createPost(String groupId, PostDTO postRequest) {
        // Check if group exists in TheKnowledgeBay
        boolean groupExists = false;
        DoublyLinkedList<StudyGroup> coreGroups = theKnowledgeBay.getStudyGroups();
        if (coreGroups != null) {
            for (int i = 0; i < coreGroups.getSize(); i++) {
                StudyGroup sg = coreGroups.get(i);
                if (sg != null && sg.getId() != null && sg.getId().equals(groupId)) {
                    groupExists = true;
                    break;
                }
            }
        }

        if (!groupExists) {
            // And if not a manually tracked group (if we had such a list here - currently we don't explicitly store manual groups in a separate list in this service for this check)
            // For now, we only check TheKnowledgeBay
            throw new NoSuchElementException("Group not found: " + groupId + " for creating post.");
        }

        String postId = "post" + postCounter.incrementAndGet();
        postRequest.setId(postId);
        postRequest.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")));
        if (postRequest.getComments() == null) {
            postRequest.setComments(new ArrayList<>());
        }
        groupPosts.computeIfAbsent(groupId, k -> new ArrayList<>()).add(0, postRequest); 
        return postRequest;
    }

    // This method is for MANUAL group creation by an admin/moderator.
    // It does NOT use TheKnowledgeBay's automatic creation logic.
    // It would need to coordinate with TheKnowledgeBay if we want a unified list of all groups.
    // For now, it's a separate mechanism if ever used.
    public StudyGroupDTO createManualStudyGroup(StudyGroupDTO groupRequest) {
        String groupId = "manual-group-" + manualGroupCounter.incrementAndGet();
        groupRequest.setId(groupId); // Set the generated ID
        // How to store this? For now, it won't appear in getAllGroups unless merged.
        // This implies manual groups would need their own storage if they aren't added to TheKnowledgeBay.studyGroups
        System.out.println("Manual study group creation requested (not added to central list): " + groupRequest.getName());
        // If we wanted to add it to the central list (potentially with checks to avoid conflicts with auto-groups):
        // StudyGroup modelToCreate = new StudyGroup();
        // modelToCreate.setId(groupId);
        // modelToCreate.setName(groupRequest.getName());
        // Interest topic = theKnowledgeBay.findInterestByName(groupRequest.getInterest()); // you'd need this helper in TKB
        // modelToCreate.setTopic(topic);
        // theKnowledgeBay.getStudyGroups().addLast(modelToCreate);
        groupPosts.putIfAbsent(groupId, new ArrayList<>()); 
        return groupRequest;
    }
} 