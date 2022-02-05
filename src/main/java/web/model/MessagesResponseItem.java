package web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class MessagesResponseItem {
    @Getter @Setter
    private String userName;

    @Getter @Setter
    private String date;

    @Getter @Setter
    private String messageText;

    @Getter @Setter
    private int votes;

    public MessagesResponseItem(String userName, String date, String messageText, int votes) {
        this.userName = userName;
        this.date = date;
        this.messageText = messageText;
        this.votes = votes;
    }
}
