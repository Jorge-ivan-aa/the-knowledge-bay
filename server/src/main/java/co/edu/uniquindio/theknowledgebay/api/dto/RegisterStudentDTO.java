package co.edu.uniquindio.theknowledgebay.api.dto;

// Eliminar la importación de DoublyLinkedList si ya no se usa directamente aquí
// import co.edu.uniquindio.theknowledgebay.infrastructure.util.datastructures.lists.DoublyLinkedList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List; // Asegurar que java.util.List esté importado

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentDTO {
    private String username;
    private String email;
    private String password;
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String biography;
    private List<String> interests; // Cambiado de DoublyLinkedList<String> a List<String>
    // Add other fields from Student if needed for registration, e.g., username, lastName
    // For simplicity, starting with basic fields.
}