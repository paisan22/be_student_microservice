package nl.ipsenh.student.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by paisanrietbroek on 07/12/2017.
 */

@RestController
@RequestMapping(value = "/internship")
@CrossOrigin(value = "*")
public class InternshipAPI {

    private final String companyAPI = "http://145.97.16.183:8081/";
    private String token = "";

    @PostMapping("/create")
    public String createInternship(@RequestBody HashMap<String, String> hashMap) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("company_id", hashMap.get("company_id"));
        jsonObject.put("title", hashMap.get("title"));
        jsonObject.put("description", hashMap.get("description"));
        jsonObject.put("start_date", hashMap.get("start_date")); // yyyy-mm-dd
        jsonObject.put("end_date", hashMap.get("end_date"));
        jsonObject.put("training_type", hashMap.get("internship_type"));
        jsonObject.put("study_type", hashMap.get("study_specialization"));

        String resource = companyAPI + "job_offer";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toJSONString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(resource, httpEntity, String.class);

        return stringResponseEntity.getStatusCode().toString();
    }

    

    @GetMapping("/token")
    public void setToken() throws IOException {

        String resource = companyAPI + "token";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(resource, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        JsonNode token = jsonNode.get("token");

        this.token = token.textValue();
    }
}
