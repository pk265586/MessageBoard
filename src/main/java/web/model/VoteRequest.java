package web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Input class for messages/vote endpoint
 */
public class VoteRequest {
    @Getter @Setter
    int id;

    @Getter @Setter
    boolean plus;
}
