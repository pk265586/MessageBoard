package web.model;

import lombok.Getter;
import lombok.Setter;

public class EditMessageRequest {
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String text;
}
