package model;

import lombok.Getter;
import lombok.Setter;

public class VoteModel {
    @Getter @Setter
    private int id;

    @Getter @Setter
    private int userId;

    @Getter @Setter
    private int messageId;

    @Getter @Setter
    private int voteValue;

    public VoteModel(){
    }

    public VoteModel(int userId, int messageId, int voteValue){
        this.userId = userId;
        this.messageId = messageId;
        this.voteValue = voteValue;
    }
}
