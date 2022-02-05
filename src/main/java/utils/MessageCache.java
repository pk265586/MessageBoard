package utils;

import lombok.Getter;
import model.MessageModel;

import java.util.ArrayList;

public class MessageCache {
    @Getter
    private ArrayList<MessageModel> cachedMessages;

    public void clear(){
        cachedMessages = null;
    }

    public void cacheResult(ArrayList<MessageModel> newMessages) {
        this.cachedMessages = newMessages;
    }

    public void addResult(ArrayList<MessageModel> newMessages){
        this.cachedMessages.addAll(newMessages);
    }
}
