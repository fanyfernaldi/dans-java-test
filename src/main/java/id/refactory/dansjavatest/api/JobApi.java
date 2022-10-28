package id.refactory.dansjavatest.api;

import id.refactory.dansjavatest.responses.BaseResponse;
import id.refactory.dansjavatest.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("api")
public class JobApi {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthUtil authUtil;
    @Value("${dans.baseUrl}")
    private String baseUrl;

    public JobApi(AuthUtil authUtil) {
        this.authUtil = authUtil;
    }

    @GetMapping(value = "/job", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getJobs(@RequestHeader("Authorization") String tokenHeader) {
        String tokenUser = tokenHeader.replace("Bearer", "");

        if (!tokenHeader.startsWith("Bearer ")) {
            String message = "invalid token";
            return BaseResponse.error(HttpStatus.UNAUTHORIZED, message);
        }

        try {
            if (!authUtil.validateToken(tokenUser)) {
                String message = "invalid token";
                return BaseResponse.error(HttpStatus.UNAUTHORIZED, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = "invalid token";
            return BaseResponse.error(HttpStatus.UNAUTHORIZED, message);
        }

        try {
            String uri = baseUrl + "recruitment/positions.json";
            String response = restTemplate.getForObject(uri, String.class);

            System.out.println("uri baseUrl " + uri);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        }
    }

    @GetMapping(value = "/job/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getJobDetail(@PathVariable String id, @RequestHeader("Authorization") String tokenHeader) {
        String tokenUser = tokenHeader.replace("Bearer", "");

        if (!tokenHeader.startsWith("Bearer ")) {
            String message = "invalid token";
            return BaseResponse.error(HttpStatus.UNAUTHORIZED, message);
        }

        try {
            if (!authUtil.validateToken(tokenUser)) {
                String message = "invalid token";
                return BaseResponse.error(HttpStatus.UNAUTHORIZED, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = "invalid token";
            return BaseResponse.error(HttpStatus.UNAUTHORIZED, message);
        }

        try {
            String uri = baseUrl + "recruitment/positions/" + id;
            String response = restTemplate.getForObject(uri, String.class);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
