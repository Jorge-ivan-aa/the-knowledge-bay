package co.edu.uniquindio.theknowledgebay.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import co.edu.uniquindio.theknowledgebay.util.datastructures.lists.DoublyLinkedList;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class Student extends User {

    private DoublyLinkedList<Interest> interests;
    private DoublyLinkedList<Content> publishedContents;
    private DoublyLinkedList<HelpRequest> helpRequests;
    private DoublyLinkedList<StudyGroup> studyGroups;
    private DoublyLinkedList<Chat> chats;
    
    private String username;
    private String lastName;
    private LocalDate dateBirth;
    private String biography;
    
    public void register() {
        // TODO: implement functionality
    }
    
    public void publishContent(Content c) {
        // TODO: implement functionality
    }
    
    public void likeContent(Content c) {
        // TODO: implement functionality
    }
    
    public void unlikeContent(Content c) {
        // TODO: implement functionality
    }
    
    public void requestHelp(HelpRequest r) {
        // TODO: implement functionality
    }
    
    public DoublyLinkedList<Student> seeStudySuggestions() {
        // TODO: implement functionality
        return null;
    }

    public DoublyLinkedList<Student> suggestContacts(Student s) {
        // TODO: implement functionality
        return null;
    }

    public void sendMessage(Message m) {
        // TODO: implement functionality
    }
    
    @Override
    public boolean login() {
        // TODO: implement functionality
        return false;
    }
    
    @Override
    public void logout() {
        // TODO: implement functionality
    }
}
