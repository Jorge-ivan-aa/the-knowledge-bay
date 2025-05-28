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
        // Initialize with some data (equivalent to mock data before)
        UserSummary userAna = new UserSummary("userAna", "Ana C.");
        UserSummary userLuis = new UserSummary("userLuis", "Luis P.");
        UserSummary userElena = new UserSummary("userElena", "Elena R.");
        UserSummary userPedro = new UserSummary("userPedro", "Pedro G.");
        UserSummary userSofia = new UserSummary("userSofia", "Sofia V.");

        StudyGroupDTO group1 = new StudyGroupDTO("group" + groupCounter.incrementAndGet(), "Amantes de React", "React.js", 23);
        StudyGroupDTO group2 = new StudyGroupDTO("group" + groupCounter.incrementAndGet(), "Aventuras en Python", "Python", 45);
        StudyGroupDTO group3 = new StudyGroupDTO("group" + groupCounter.incrementAndGet(), "Diseño UI/UX Pro", "UI/UX", 18);
        StudyGroupDTO group4 = new StudyGroupDTO("group" + groupCounter.incrementAndGet(), "Nómadas Digitales Dev", "Desarrollo Web", 33);

        studyGroups.put(group1.getId(), group1);
        studyGroups.put(group2.getId(), group2);
        studyGroups.put(group3.getId(), group3);
        studyGroups.put(group4.getId(), group4);

        List<PostDTO> postsGroup1 = new ArrayList<>();
        postsGroup1.add(new MarkdownPostDTO("post" + postCounter.incrementAndGet(), userAna, "Hace 2 horas", 15, 
            "¡Hola equipo! Acabo de terminar un tutorial increíble sobre los nuevos Hooks en React 19. ¿Alguien más lo ha visto? \n\nPrincipales puntos: \n* `useOptimistic` para UI más rápidas. \n* Mejoras en `useMemo` y `useCallback`. \n\n¡Compartan sus pensamientos!"));
        postsGroup1.get(0).getComments().add(new CommentDTO("c" + commentCounter.incrementAndGet(), new UserSummary("userCarlos", "Carlos M."), "¡Suena genial! Voy a revisarlo.", "Hace 1 hora"));
        
        postsGroup1.add(new YouTubePostDTO("post" + postCounter.incrementAndGet(), userLuis, "Hace 5 horas", 22, "Curso Intensivo de Tailwind CSS", "rokGy0huYEA", "Un excelente curso para dominar Tailwind CSS desde cero."));
        postsGroup1.add(new LinkPostDTO("post" + postCounter.incrementAndGet(), userElena, "Hace 1 día", 10, "Documentación Oficial de Next.js 14", "https://nextjs.org/docs", "Siempre es bueno tener a mano la documentación oficial."));
        postsGroup1.add(new HelpRequestPostDTO("post" + postCounter.incrementAndGet(), userPedro, "Hace 3 horas", 5, "¿Alguien podría ayudarme a entender cómo funciona el context API en comparación con Redux para un proyecto pequeño?", "Estoy construyendo una app simple y no estoy seguro si Redux es demasiado. ¿El Context API sería suficiente para manejar el estado de autenticación y el tema (oscuro/claro)?"));
        groupPosts.put(group1.getId(), postsGroup1);

        List<PostDTO> postsGroup2 = new ArrayList<>();
        postsGroup2.add(new MarkdownPostDTO("post" + postCounter.incrementAndGet(), userSofia, "Hace 45 minutos", 18, "Descubrí una librería de Python para visualización de datos llamada `Plotly`. Es interactiva y muy potente. \n\n```python\nimport plotly.express as px\ndf = px.data.iris()\nfig = px.scatter(df, x=\"sepal_width\", y=\"sepal_length\", color=\"species\")\nfig.show()\n```"));
        groupPosts.put(group2.getId(), postsGroup2);
        
        // Initialize empty post lists for other groups to avoid nulls
        groupPosts.putIfAbsent(group3.getId(), new ArrayList<>());
        groupPosts.putIfAbsent(group4.getId(), new ArrayList<>());
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
        
        groupPosts.computeIfAbsent(groupId, k -> new ArrayList<>()).add(0, postRequest); // Add to beginning
        return postRequest;
    }

    // Placeholder for creating a new group - not fully implemented as per student restrictions
    public StudyGroupDTO createStudyGroup(StudyGroupDTO groupRequest) {
        String groupId = "group" + groupCounter.incrementAndGet();
        groupRequest.setId(groupId);
        studyGroups.put(groupId, groupRequest);
        groupPosts.putIfAbsent(groupId, new ArrayList<>());
        return groupRequest;
    }
} 