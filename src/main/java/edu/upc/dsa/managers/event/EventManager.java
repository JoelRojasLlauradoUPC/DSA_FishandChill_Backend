package edu.upc.dsa.managers.event;

import edu.upc.dsa.services.dto.EventUser;
import org.apache.log4j.Logger;

import java.util.*;

public class EventManager {

    final static Logger logger = Logger.getLogger(EventManager.class);

    private static final Map<String, List<EventUser>> registrations = new HashMap<>();
    private static final Map<String, String> eventTitles = new HashMap<>();

    static {
        eventTitles.put("1", "Fishing Storm");
        eventTitles.put("2", "Meteor Arrival");

        registrations.put("1", Arrays.asList(
                new EventUser(
                        "Juan",
                        "Pérez García",
                        "https://cdn.pixabay.com/photo/2017/07/11/15/51/kermit-2493979_1280.png"
                ),
                new EventUser(
                        "Laura",
                        "Sánchez Ruiz",
                        "https://cdn.pixabay.com/photo/2016/01/10/18/59/cookie-monster-1132275_1280.jpg"
                ),
                new EventUser(
                        "Marc",
                        "López Vidal",
                        "https://cdn.pixabay.com/photo/2014/04/03/10/32/user-310807_1280.png"
                )
        ));

        registrations.put("2", Arrays.asList(
                new EventUser(
                        "Paula",
                        "Gómez Martín",
                        "https://cdn.pixabay.com/photo/2014/04/03/10/32/businessman-310819_1280.png"
                )
        ));
    }

    public static List<EventUser> getRegisteredUsers(String eventId) {
        logger.info("getRegisteredUsers: eventId=" + eventId);

        List<EventUser> users = registrations.get(eventId);
        if (users == null) return new ArrayList<>();

        return new ArrayList<>(users);
    }

    public static String getEventTitle(String eventId) {
        String title = eventTitles.get(eventId);
        return title != null ? title : ("Event " + eventId);
    }
}
