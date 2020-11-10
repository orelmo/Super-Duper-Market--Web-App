package EngineClasses.ChatManager;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private List<String> chatHistory = new ArrayList<>();

    public void addNewMessage(String newMessage) {
        this.chatHistory.add(newMessage);
    }

    public List<String> getDeltaMessages(Integer seenMessages) {
        List<String> deltaMessages = new ArrayList<>();

        for(int i = 0; i < this.chatHistory.size(); ++i){
            if(i >= seenMessages){
                deltaMessages.add(this.chatHistory.get(i));
            }
        }

        return deltaMessages;
    }
}