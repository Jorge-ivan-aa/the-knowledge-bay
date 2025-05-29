package co.edu.uniquindio.theknowledgebay.core.service;

import co.edu.uniquindio.theknowledgebay.core.model.TheKnowledgeBay;
import co.edu.uniquindio.theknowledgebay.core.model.Student;
import co.edu.uniquindio.theknowledgebay.core.model.Interest;
import co.edu.uniquindio.theknowledgebay.core.model.Content;
import co.edu.uniquindio.theknowledgebay.core.model.HelpRequest;
import co.edu.uniquindio.theknowledgebay.core.model.enums.ContentType;
import co.edu.uniquindio.theknowledgebay.core.model.enums.Urgency;
import co.edu.uniquindio.theknowledgebay.infrastructure.util.datastructures.lists.DoublyLinkedList;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TestDataLoaderService {

    private final TheKnowledgeBay theKnowledgeBay;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    private int userIdCounter = 1; // Para IDs secuenciales

    public TestDataLoaderService(TheKnowledgeBay theKnowledgeBay, PasswordEncoder passwordEncoder) {
        this.theKnowledgeBay = theKnowledgeBay;
        this.passwordEncoder = passwordEncoder;
    }

    private LocalDate getRandomDateInMay(int year) {
        int day = random.nextInt(29) + 1; // Days from 1 to 29
        return LocalDate.of(year, Month.MAY, day);
    }

    private LocalDate getRandomBirthDate(int startYear, int endYear) {
        long minDay = LocalDate.of(startYear, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(endYear, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1);
        return LocalDate.ofEpochDay(randomDay);
    }

    public void loadComprehensiveTestData() {
        System.out.println("Iniciando carga de datos de prueba comprehensivos...");
        int currentYear = LocalDate.now().getYear();

        // Crear Intereses (traducidos donde aplique)
        System.out.println("Creando intereses...");
        Interest interest1 = createAndAddInterest("Programación en Java");
        Interest interest2 = createAndAddInterest("Spring Framework"); // Framework name, keep as is
        Interest interest3 = createAndAddInterest("Estructuras de Datos");
        Interest interest4 = createAndAddInterest("Algoritmos");
        Interest interest5 = createAndAddInterest("Aprendizaje Automático"); // Machine Learning

        // Crear Usuarios (Estudiantes)
        System.out.println("Creando estudiantes...");
        List<Student> students = new ArrayList<>();

        Student student1 = createStudent("juan.perez", "juan.perez@example.com", "clave123", "Juan", "Pérez", getRandomBirthDate(2000, 2005), "Biografía de Juan, entusiasta de Java.", createInterestList(interest1, interest3));
        Student student2 = createStudent("maria.lopez", "maria.lopez@example.com", "clave123", "María", "López", getRandomBirthDate(2000, 2005), "Biografía de María, enfocada en algoritmos.", createInterestList(interest2, interest4));
        Student student3 = createStudent("carlos.gomez", "carlos.gomez@example.com", "clave123", "Carlos", "Gómez", getRandomBirthDate(2000, 2005), "Biografía de Carlos, apasionado por el Machine Learning.", createInterestList(interest1, interest2, interest5));
        Student student4 = createStudent("ana.martinez", "ana.martinez@example.com", "clave123", "Ana", "Martínez", getRandomBirthDate(2000, 2005), "Biografía de Ana, experta en estructuras de datos.", createInterestList(interest3, interest4));
        Student student5 = createStudent("luis.fernandez", "luis.fernandez@example.com", "clave123", "Luis", "Fernández", getRandomBirthDate(2000, 2005), "Biografía de Luis, explorando nuevas tecnologías.", createInterestList(interest5));
        Student student6 = createStudent("sofia.rodriguez", "sofia.rodriguez@example.com", "clave123", "Sofía", "Rodríguez", getRandomBirthDate(2000, 2005), "Biografía de Sofía, desarrolladora full-stack.", createInterestList(interest1, interest4, interest2));

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);

        System.out.println("Agregando estudiantes al sistema...");
        students.forEach(student -> {
            theKnowledgeBay.addStudent(student);
            System.out.println("Estudiante agregado: " + student.getUsername() + " con ID: " + student.getId());
        });

        // Verificar que los estudiantes fueron agregados correctamente
        System.out.println("Verificando estudiantes agregados...");
        System.out.println("Total de estudiantes en el sistema: " + theKnowledgeBay.getUsers().getStudents().getSize());

        // After all students are added, then update/create study groups for them
        System.out.println("Actualizando grupos de estudio automáticos...");
        students.forEach(student -> {
            System.out.println("Actualizando grupos para: " + student.getUsername());
            theKnowledgeBay.updateAutomaticStudyGroupsForStudent(student);
        });

        // Verificar grupos creados
        System.out.println("Grupos de estudio creados: " + theKnowledgeBay.getStudyGroups().getSize());

        // Crear Contenido para los usuarios
        // Estudiante 1 (Juan Pérez)
        createContentForStudent(student1, "Entendiendo Genéricos en Java", ContentType.ARTICLE, "Explicación detallada de los genéricos en Java.", createInterestList(interest1), getRandomDateInMay(currentYear));
        createContentForStudent(student1, "Introducción a Spring Boot", ContentType.ARTICLE, "Primeros pasos con aplicaciones Spring Boot.", createInterestList(interest2), getRandomDateInMay(currentYear));
        createContentForStudent(student1, "¿Mejores IDEs para Java en 2024?", ContentType.QUESTION, "Buscando recomendaciones de la comunidad sobre los mejores IDEs de Java para productividad en 2024.", createInterestList(interest1), getRandomDateInMay(currentYear));
        createContentForStudent(student1, "Documentación Oficial de Spring Framework", ContentType.LINK, "https://spring.io/docs", createInterestList(interest2), getRandomDateInMay(currentYear));

        // Estudiante 2 (María López)
        createContentForStudent(student2, "Notación Big O Explicada", ContentType.RESOURCE, "Guía comprensiva para entender la notación Big O para análisis de algoritmos y eficiencia.", createInterestList(interest4, interest3), getRandomDateInMay(currentYear));
        createContentForStudent(student2, "Quick Sort vs Merge Sort", ContentType.ARTICLE, "Análisis comparativo de los algoritmos Quick Sort y Merge Sort, discutiendo pros, contras y casos de uso.", createInterestList(interest4, interest3), getRandomDateInMay(currentYear));
        createContentForStudent(student2, "Visualizando Algoritmos", ContentType.VIDEO, "http://example.com/algo-viz - Un sitio genial que visualiza algoritmos de ordenamiento y búsqueda de caminos.", createInterestList(interest4), getRandomDateInMay(currentYear));

        // Estudiante 3 (Carlos Gómez)
        createContentForStudent(student3, "Papers de ML Favoritos", ContentType.LINK, "Lista curada de papers influyentes en machine learning. http://example.com/ml-papers", createInterestList(interest5), getRandomDateInMay(currentYear));
        createContentForStudent(student3, "Primeros Pasos con TensorFlow", ContentType.ARTICLE, "Tutorial amigable para principiantes sobre cómo configurar TensorFlow y construir tu primera red neuronal.", createInterestList(interest5), getRandomDateInMay(currentYear));
        createContentForStudent(student3, "Top 5 Librerías de Machine Learning", ContentType.RESOURCE, "Reseña de las 5 principales librerías de Python para machine learning: Scikit-learn, TensorFlow, Keras, PyTorch y XGBoost.", createInterestList(interest5), getRandomDateInMay(currentYear));

        // Estudiante 4 (Ana Martínez)
        createContentForStudent(student4, "Algoritmos de Ordenamiento Visualizados", ContentType.VIDEO, "Video explicativo de algoritmos de ordenamiento comunes como bubble sort, insertion sort y selection sort. http://example.com/sorting-video", createInterestList(interest4), getRandomDateInMay(currentYear));
        createContentForStudent(student4, "Errores Comunes en Estructuras de Datos", ContentType.ARTICLE, "Destacando errores frecuentes que los estudiantes cometen al implementar y usar estructuras de datos comunes.", createInterestList(interest3), getRandomDateInMay(currentYear));
        createContentForStudent(student4, "Consejo para aprender algoritmos avanzados", ContentType.QUESTION, "Estoy cómodo con algoritmos básicos, ¿qué recursos o caminos recomiendan para aprender temas más avanzados como programación dinámica o teoría de grafos?", createInterestList(interest4), getRandomDateInMay(currentYear));

        // Estudiante 5 (Luis Fernández)
        createContentForStudent(student5, "¿Es Python bueno para ML?", ContentType.QUESTION, "Discutiendo los pros y contras de Python en proyectos de Machine Learning comparado con otros lenguajes como R o Java.", createInterestList(interest5), getRandomDateInMay(currentYear));
        createContentForStudent(student5, "Reseña de la Especialización en Deep Learning", ContentType.LINK, "http://example.com/deeplearning-review - Mis pensamientos sobre la Especialización en Deep Learning de Coursera por Andrew Ng.", createInterestList(interest5), getRandomDateInMay(currentYear));

        // Estudiante 6 (Sofía Rodríguez)
        createContentForStudent(student6, "Mejores Prácticas de Concurrencia en Java", ContentType.ARTICLE, "Consejos y patrones para escribir aplicaciones concurrentes robustas, escalables y mantenibles en Java.", createInterestList(interest1), getRandomDateInMay(currentYear));
        createContentForStudent(student6, "Tutorial de Microservicios con Spring Boot", ContentType.VIDEO, "http://example.com/springboot-microservices - Guía paso a paso para construir microservicios con Spring Boot y Spring Cloud.", createInterestList(interest2, interest1), getRandomDateInMay(currentYear));
        createContentForStudent(student6, "¿Cómo prepararse para una entrevista técnica de Java?", ContentType.QUESTION, "¿Cuáles son las áreas clave en las que enfocarse y las preguntas comunes en las entrevistas técnicas de Java?", createInterestList(interest1, interest4), getRandomDateInMay(currentYear));

        // Crear Solicitudes de Ayuda para los usuarios
        createHelpRequestForStudent(student1, "Necesito ayuda con la configuración de Spring Security.", Urgency.HIGH, createInterestList(interest2), false, getRandomDateInMay(currentYear));
        createHelpRequestForStudent(student2, "Atascado en un problema de algoritmos de grafos.", Urgency.MEDIUM, createInterestList(interest4), true, getRandomDateInMay(currentYear)); // Marcada como completada
        createHelpRequestForStudent(student4, "Entendiendo la recursión para estructuras de datos.", Urgency.LOW, createInterestList(interest3), false, getRandomDateInMay(currentYear));
        createHelpRequestForStudent(student5, "Eligiendo un tema de doctorado en ML.", Urgency.CRITICAL, createInterestList(interest5), false, getRandomDateInMay(currentYear));

        // Establecer relaciones de Seguimiento
        // student1 sigue a student2, student3
        theKnowledgeBay.followUser(student1.getId(), student2.getId());
        theKnowledgeBay.followUser(student1.getId(), student3.getId());

        // student2 sigue a student1
        theKnowledgeBay.followUser(student2.getId(), student1.getId());

        // student3 sigue a student1, student4, student5
        theKnowledgeBay.followUser(student3.getId(), student1.getId());
        theKnowledgeBay.followUser(student3.getId(), student4.getId());
        theKnowledgeBay.followUser(student3.getId(), student5.getId());

        // student4 sigue a student2, student6
        theKnowledgeBay.followUser(student4.getId(), student2.getId());
        theKnowledgeBay.followUser(student4.getId(), student6.getId());
        
        // student5 sigue a student3
        theKnowledgeBay.followUser(student5.getId(), student3.getId());
        
        // student6 sigue a student1, student2, student3, student4
        theKnowledgeBay.followUser(student6.getId(), student1.getId());
        theKnowledgeBay.followUser(student6.getId(), student2.getId());
        theKnowledgeBay.followUser(student6.getId(), student3.getId());
        theKnowledgeBay.followUser(student6.getId(), student4.getId());

        // Verificación final del estado del sistema
        System.out.println("\n=== VERIFICACIÓN FINAL DEL SISTEMA ===");
        System.out.println("Total de usuarios: " + theKnowledgeBay.getUsers().getStudents().getSize());
        System.out.println("Total de grupos de estudio: " + theKnowledgeBay.getStudyGroups().getSize());
        System.out.println("Total de contenido: " + theKnowledgeBay.getAllContent().getSize());
        System.out.println("Total de solicitudes de ayuda: " + theKnowledgeBay.getAllHelpRequests().getSize());
        System.out.println("Total de intereses: " + theKnowledgeBay.getAllInterests().getSize());
        
        // Verificar datos específicos para cada usuario creado
        for (Student student : students) {
            System.out.println("\nUsuario: " + student.getUsername() + " (ID: " + student.getId() + ")");
            System.out.println("  - Contenidos publicados: " + (student.getPublishedContents() != null ? student.getPublishedContents().getSize() : 0));
            System.out.println("  - Solicitudes de ayuda: " + (student.getHelpRequests() != null ? student.getHelpRequests().getSize() : 0));
            System.out.println("  - Grupos de estudio: " + (student.getStudyGroups() != null ? student.getStudyGroups().getSize() : 0));
            System.out.println("  - Intereses: " + (student.getInterests() != null ? student.getInterests().getSize() : 0));
            
            // Verificar estadísticas usando los métodos de TheKnowledgeBay
            int contentCount = theKnowledgeBay.getContentCountByUserId(student.getId());
            int helpRequestCount = theKnowledgeBay.getHelpRequestCountByUserId(student.getId());
            int groupCount = theKnowledgeBay.getUserStudyGroupCount(student.getId());
            System.out.println("  - Estadísticas del sistema:");
            System.out.println("    * Contenidos: " + contentCount);
            System.out.println("    * Solicitudes: " + helpRequestCount);
            System.out.println("    * Grupos: " + groupCount);
        }

        System.out.println("\nDatos de prueba comprensivos cargados en español con fechas aleatorias.");
        System.out.println("=== FIN DE LA CARGA DE DATOS ===\n");
    }

    private Interest createAndAddInterest(String name) {
        Interest interest = Interest.builder()
                .idInterest(UUID.randomUUID().toString())
                .name(name)
                .build();
        theKnowledgeBay.addInterest(interest);
        return interest;
    }

    private Student createStudent(String username, String email, String rawPassword, String firstName, String lastName, LocalDate dob, String bio, DoublyLinkedList<Interest> interests) {
        String studentId = String.valueOf(userIdCounter++); // Usar ID secuencial
        System.out.println("Creando estudiante con ID: " + studentId + ", username: " + username);
        
        Student student = Student.builder()
                .id(studentId)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .firstName(firstName)
                .lastName(lastName)
                .dateBirth(dob)
                .biography(bio)
                .interests(interests)
                .publishedContents(new DoublyLinkedList<>())
                .helpRequests(new DoublyLinkedList<>()) 
                .following(new DoublyLinkedList<>()) 
                .followers(new DoublyLinkedList<>()) 
                .studyGroups(new DoublyLinkedList<>()) 
                .chats(new DoublyLinkedList<>()) 
                .build();
                
        System.out.println("Estudiante creado exitosamente: " + student.getId() + " - " + student.getUsername());
        return student;
    }

    private void createContentForStudent(Student author, String title, ContentType type, String information, DoublyLinkedList<Interest> topics, LocalDate date) {
        System.out.println("Creando contenido '" + title + "' para usuario: " + author.getUsername() + " (ID: " + author.getId() + ")");
        
        Content content = Content.builder()
                .title(title)
                .contentType(type)
                .information(information)
                .author(author)
                .topics(topics)
                .likedBy(new DoublyLinkedList<>()) 
                .likeCount(0)
                .comments(new DoublyLinkedList<>()) 
                .date(date)
                .build();
                
        boolean contentAdded = theKnowledgeBay.addContent(content);
        System.out.println("Contenido agregado al sistema: " + contentAdded + " - ID del contenido: " + content.getContentId());
        
        if (author.getPublishedContents() == null) {
            author.setPublishedContents(new DoublyLinkedList<>());
        }
        author.getPublishedContents().addLast(content);
        System.out.println("Contenido agregado a la lista del autor. Total contenidos del autor: " + author.getPublishedContents().getSize());
    }

    private void createHelpRequestForStudent(Student student, String information, Urgency urgency, DoublyLinkedList<Interest> topics, boolean isCompleted, LocalDate date) {
        System.out.println("Creando solicitud de ayuda para usuario: " + student.getUsername() + " (ID: " + student.getId() + ")");
        
        HelpRequest helpRequest = HelpRequest.builder()
                .student(student)
                .information(information)
                .urgency(urgency)
                .topics(topics)
                .isCompleted(isCompleted)
                .requestDate(date)
                .comments(new DoublyLinkedList<>()) 
                .build();
                
        boolean requestAdded = theKnowledgeBay.addHelpRequest(helpRequest);
        System.out.println("Solicitud de ayuda agregada al sistema: " + requestAdded + " - ID de la solicitud: " + helpRequest.getRequestId());
        
        if (student.getHelpRequests() == null) {
            student.setHelpRequests(new DoublyLinkedList<>());
        }
        student.getHelpRequests().addLast(helpRequest);
        System.out.println("Solicitud agregada a la lista del estudiante. Total solicitudes del estudiante: " + student.getHelpRequests().getSize());
    }

    private DoublyLinkedList<Interest> createInterestList(Interest... interests) {
        DoublyLinkedList<Interest> list = new DoublyLinkedList<>();
        for (Interest interest : interests) {
            list.addLast(interest);
        }
        return list;
    }
} 