package EngineClasses.Feedback;

import java.util.ArrayList;
import java.util.List;

public class Feedbacks {
    List<Feedback> feedbacks = new ArrayList<>();

    public void add(Feedback feedback) {
        this.feedbacks.add(feedback);
    }

    public List<Feedback> getFeedbacks() {
        return this.feedbacks;
    }
}