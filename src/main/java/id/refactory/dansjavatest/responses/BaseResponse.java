package id.refactory.dansjavatest.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class BaseResponse<T> {
    @JsonProperty
    private boolean error;
    @JsonProperty
    private String message;
    @JsonProperty
    private T data;

    @JsonCreator
    public BaseResponse(boolean error, String message, T data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<?> error(HttpStatus status, String message) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", message);

        return new ResponseEntity(response, status);
    }
}
