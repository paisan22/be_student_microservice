package nl.ipsenh.student.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by paisanrietbroek on 07/12/2017.
 */

@RestController
@RequestMapping(value = "/company")
@CrossOrigin(value = "*")
public class CompanyAPI {

    private final String companyAPI = "http://145.97.16.183:8081/";

    @PostMapping("/internship/create")
    public String createInternship(@RequestBody HashMap<String, String> hashMap) throws IOException, ParseException {

        String resource = companyAPI + "job_offer";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("company_id", hashMap.get("company_id"));
        jsonObject.put("title", hashMap.get("title"));
        jsonObject.put("description", hashMap.get("description"));
        jsonObject.put("start_date", hashMap.get("start_date")); // yyyy-mm-dd
        jsonObject.put("end_date", hashMap.get("end_date"));
        jsonObject.put("training_type", hashMap.get("internship_type"));

        String study_specialization = hashMap.get("study_specialization");
        JSONParser jsonParser = new JSONParser();
        JSONArray parse = (JSONArray) jsonParser.parse(study_specialization);

        jsonObject.put("study_type", parse);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", getToken());

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toJSONString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(resource, httpEntity, String.class);

        return stringResponseEntity.getStatusCode().toString();
    }

    @GetMapping(value = "/internship")
    public JSONArray getAllInternships() throws IOException {
        String resource = companyAPI + "job_offer";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", getToken());

        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);

        return getRequest(resource, parameters);
    }

    @GetMapping(value = "internship/{id}")
    public JSONObject getInternship(@PathVariable("id") String id) throws IOException, ParseException {
        String resource = companyAPI + "job_offer/" + id;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", getToken());

        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(resource, HttpMethod.GET, parameters, String.class);

        String jsonString = exchange.getBody();
        JSONParser jsonParser = new JSONParser();
        Object parse = jsonParser.parse(jsonString);

        return (JSONObject) parse;
    }

    @PostMapping
    public String createCompany(@RequestBody HashMap<String, String> hashMap) throws IOException {

        String resource = companyAPI + "/company";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("company_name", hashMap.get("company_name"));
        jsonObject.put("address", hashMap.get("address"));
        jsonObject.put("zipcode", hashMap.get("zipcode"));
        jsonObject.put("city", hashMap.get("city"));
        jsonObject.put("website", hashMap.get("website"));

        JSONArray contactPersonList = new JSONArray();
        JSONObject contactPerson = new JSONObject();
        contactPerson.put("person_name", hashMap.get("person_name"));
        contactPerson.put("phonenumber", hashMap.get("phonenumber"));
        contactPerson.put("email", hashMap.get("email"));
        contactPersonList.add(contactPerson);

        jsonObject.put("contact_person", contactPersonList);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", getToken());

        HttpEntity<String> parameters = new HttpEntity<String>(jsonObject.toJSONString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(resource, parameters, String.class);
            return stringResponseEntity.getBody();
        } catch (HttpClientErrorException e) {
            return e.getStatusCode().toString();
        }
    }

    @GetMapping
    public JSONArray getAllCompanies() throws IOException {

        String resource = companyAPI + "company";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", getToken());

        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);
        return getRequest(resource, parameters);
    }

    private String getToken() throws IOException {

        String resource = companyAPI + "token";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(resource, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        JsonNode token = jsonNode.get("token");

        return token.textValue();
    }

    private JSONArray getRequest(String resource, HttpEntity parameters) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> exchange = restTemplate.exchange(resource, HttpMethod.GET, parameters, String.class);

            String jsonString = exchange.getBody();
            JSONParser jsonParser = new JSONParser();
            Object parse = jsonParser.parse(jsonString);

            return (JSONArray) parse;

        } catch (HttpClientErrorException e) {
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
