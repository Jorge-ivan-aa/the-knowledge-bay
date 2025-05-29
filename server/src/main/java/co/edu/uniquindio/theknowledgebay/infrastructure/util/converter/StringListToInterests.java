package co.edu.uniquindio.theknowledgebay.infrastructure.util.converter;

import co.edu.uniquindio.theknowledgebay.core.model.Interest;
import co.edu.uniquindio.theknowledgebay.infrastructure.util.datastructures.lists.DoublyLinkedList;

import java.util.ArrayList;
import java.util.List;

public class StringListToInterests {

    public static List<Interest> convert(List<String> list) {

        if (list == null) {
            return null;
        }

        List<Interest> interests = new ArrayList<>();
        for (String s : list) {
            Interest interest = new Interest();
            interest.setName(s);
            interests.add(interest);
        }

        return interests;
    }

}
