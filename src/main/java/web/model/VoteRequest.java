package web.model;

import lombok.Getter;
import lombok.Setter;

public class VoteRequest {
    @Getter @Setter
    int id;

    @Getter @Setter
    boolean plus;
}
