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
import org.springframework.web.client.HttpServerErrorException;
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

    @GetMapping(value = "/{id}")
    public JSONObject getCompanyById(@PathVariable("id") String id) throws IOException, ParseException {

        String resource = companyAPI + "company/" + id;

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
    public String createCompany(@RequestBody JSONObject jsonObject1) throws IOException {

        String resource = companyAPI + "/company";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("company_name", jsonObject1.get("company_name"));
        jsonObject.put("address", jsonObject1.get("address"));
        jsonObject.put("zipcode", jsonObject1.get("zipcode"));
        jsonObject.put("city", jsonObject1.get("city"));
        jsonObject.put("website", jsonObject1.get("website"));

        Object contact_person = jsonObject1.get("contact_person");

        jsonObject.put("contact_person", contact_person);

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


        HttpHeaders httpHeaders = createHttpHeaders();

        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);

        return getRequest(resource, parameters);
    }

    public HttpHeaders createHttpHeaders() throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        httpHeaders.add("Authorization", getToken());

        return httpHeaders;
    }

    private String getToken() throws IOException {

        try {
            String resource = companyAPI + "token";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(resource, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode token = jsonNode.get("token");

            return token.textValue();
        } catch (HttpServerErrorException e) {
            System.out.println(e.getMessage());
            return "false";
        }


    }

    private JSONArray getRequest(String resource, HttpEntity parameters) {
        try {

            if (parameters.getHeaders().get("Authorization").get(0) != "false") {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> exchange = restTemplate.exchange(resource, HttpMethod.GET, parameters, String.class);

                String jsonString = exchange.getBody();
                JSONParser jsonParser = new JSONParser();
                Object parse = jsonParser.parse(jsonString);

                return (JSONArray) parse;
            } else {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", "no token available from company API");
                jsonArray.add(jsonObject);
                return jsonArray;
            }

        } catch (HttpClientErrorException e) {
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/internship_overview_table")
    public JSONArray getDataForInternshipTable() throws IOException, ParseException {

        JSONArray jsonArray = new JSONArray();

        JSONArray allInternships = getAllInternships();

        for (Object jsonObject: allInternships) {

            String company_id = ((JSONObject) jsonObject).get("company_id").toString();
            JSONObject company = getCompanyById(company_id);

            ((JSONObject) jsonObject).put("company_name", company.get("company_name"));

            jsonArray.add((JSONObject)jsonObject);
        }
        return jsonArray;
    }
}
