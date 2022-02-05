package web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Input class for messages/top-messages endpoint
 */
public class TopMessagesRequest {
    @Getter @Setter
    int count;
}
