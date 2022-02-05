package web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Input class for users/login endpoint
 */
public class UserLoginRequest {
    @Getter @Setter
    String username;
}
