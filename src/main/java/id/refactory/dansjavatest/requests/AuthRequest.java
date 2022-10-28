package id.refactory.dansjavatest.requests;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AuthRequest {
    private String username, password;

    @JsonCreator
    public AuthRequest() {

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
