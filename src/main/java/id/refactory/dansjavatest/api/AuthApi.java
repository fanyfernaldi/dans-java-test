package id.refactory.dansjavatest.api;

import id.refactory.dansjavatest.models.UserModel;
import id.refactory.dansjavatest.repositories.UserRepository;
import id.refactory.dansjavatest.requests.AuthRequest;
import id.refactory.dansjavatest.responses.BaseResponse;
import id.refactory.dansjavatest.utils.AuthUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("api/auth")
public class AuthApi {

    private final UserRepository userRepository;

    private final AuthUtil authUtil;

    private final PasswordEncoder passwordEncoder;

    public AuthApi(UserRepository userRepository, AuthUtil authUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        if (authRequest == null) {
            String message = "Body must be filled";
            return BaseResponse.error(HttpStatus.BAD_REQUEST, message);
        }

        UserModel userModel = userRepository.findByUsername(authRequest.getUsername());

        if (userModel == null || !passwordEncoder.matches(authRequest.getPassword(), userModel.getPassword())) {
            String message = "wrong username or password";
            return BaseResponse.error(HttpStatus.BAD_REQUEST, message);
        }

        try {
            return getTokenResponse(authRequest.getUsername(), authRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();

            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {

        if (authRequest.getUsername().isEmpty() || authRequest.getPassword().isEmpty()) {
            String message = "username or password must be filled";
            return BaseResponse.error(HttpStatus.BAD_REQUEST, message);
        }

        if (authRequest.getUsername().length() <= 8 || authRequest.getPassword().length() <= 8) {
            String message = "username or password must be greater than 8 chars";
            return BaseResponse.error(HttpStatus.BAD_REQUEST, message);
        }

        String hashedPassword = passwordEncoder.encode(authRequest.getPassword());
        UserModel userModel = new UserModel(
                0L,
                authRequest.getUsername(),
                hashedPassword,
                System.currentTimeMillis()
        );

        try {
            userRepository.save(userModel);

            return getTokenResponse(authRequest.getUsername(), authRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();

            String message = "cannot register with this username";

            return BaseResponse.error(HttpStatus.BAD_REQUEST, message);
        }
    }


    private @NotNull ResponseEntity<?> getTokenResponse(String username, String password) {
        User user = getUserFromEntity(username, password);
        String token = authUtil.generateToken(user);

        HashMap<String, Object> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    private @NotNull User getUserFromEntity(String username, String password) {
        return new User(username, password, new ArrayList<>());
    }
}
