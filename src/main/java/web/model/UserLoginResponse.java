package web.model;

import lombok.Getter;
import lombok.Setter;

public class UserLoginResponse {
    @Getter @Setter
    private int id;

    public UserLoginResponse(int id) {
        this.id = id;
    }
}
