package web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Input class for messages/edit endpoint
 */
public class EditMessageRequest {
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String text;
}
