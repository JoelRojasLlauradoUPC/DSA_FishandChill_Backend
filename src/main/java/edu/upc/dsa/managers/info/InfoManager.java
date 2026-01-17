package edu.upc.dsa.managers.info;
import edu.upc.dsa.services.dto.Faq;
import edu.upc.dsa.services.dto.Video;


import edu.upc.dsa.orm.*;
import org.eclipse.persistence.sessions.factories.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InfoManager {

    public static List<Faq> getFaqs() {
        Session session = FactorySession.openSession();
        List<Faq> faqs = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<Object> result = session.get(Faq.class, params);
        session.close();
        for (Object o : result) {
            Faq faq = (Faq) o;
            if (faq.getAnswer() == null) {
                continue; // skip FAQs without an answer yet
            }
            faqs.add((Faq) o);
        }
        return faqs;
    }

    public static void postFaqQuestion(String question) {
        Faq faq = new Faq();
        faq.setQuestion(question);
        faq.setAnswer(null); // answer is null initially
        Session session = FactorySession.openSession();
        session.save(faq);
        session.close();
    }

    public static List<Video> getVideos() {
        Session session = FactorySession.openSession();
        List<Video> videos = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<Object> result = session.get(Video.class, params);
        session.close();
        for (Object o : result) {
            videos.add((Video) o);
        }
        return videos;
    }


}



