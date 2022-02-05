package web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Input class for messages/post endpoint
 */
public class PostMessageRequest {
    @Getter @Setter
    String text;
}
