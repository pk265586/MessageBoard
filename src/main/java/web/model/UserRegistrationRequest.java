package web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Input class for users/register endpoint
 */
public class UserRegistrationRequest {
    @Getter @Setter
    private String username;
}
