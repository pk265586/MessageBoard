package model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class MessageModel {
    @Getter @Setter
    private int id;

    @Getter @Setter
    private int userId;

    @Getter @Setter
    private Date date;

    @Getter @Setter
    private String text;

    @Getter @Setter
    private int votes;

    @Getter @Setter
    private String userName;

    public boolean isNew() {
        return id<=0;
    }

    public MessageModel(){
    }

    // constructor for fast creation of new messages - used in tests
    public MessageModel(int userId, Date date, String text) {
        this(0, userId, date, text, 0, "");
    }

    public MessageModel(int id, int userId, Date date, String text, int votes, String userName) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.text = text;
        this.votes = votes;
        this.userName = userName;
    }
}
