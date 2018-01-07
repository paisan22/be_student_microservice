package nl.ipsenh.student.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ipsenh.student.service.RequestService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final String CompanyAPI_internship = companyAPI + "job_offer/";
    private final String CompanyAPI_company = companyAPI + "company/";

    private static final String COMPANY_ID = "company_id";
    private static final String COMPANY_NAME = "company_name";

    @Autowired
    private RequestService requestService;

    @PostMapping("/internship/create")
    public String createInternship(@RequestBody HashMap<String, String> hashMap) throws IOException, ParseException {

        JSONObject internshipPostObject = createInternshipPostObject(hashMap);

        HttpEntity<String> companyHeader = this.requestService.createCompanyHeader(getToken(), internshipPostObject);

        ResponseEntity<String> stringResponseEntity = this.requestService.performPostRequest(CompanyAPI_internship, companyHeader);

        return stringResponseEntity.getStatusCode().toString();
    }

    @GetMapping(value = "/internship")
    public JSONArray getAllInternships() throws IOException, ParseException {

        return this.requestService.getJSONArrayRequest(this.CompanyAPI_internship, getToken());

    }

    @GetMapping(value = "internship/{id}")
    public JSONObject getInternship(@PathVariable("id") String id) throws IOException, ParseException {
        String resource = CompanyAPI_internship + id;

        return this.requestService.getJSONObjectRequest(resource, getToken());
    }

    @GetMapping(value = "/{id}")
    public JSONObject getCompanyById(@PathVariable("id") String id) throws IOException, ParseException {

        String resource = CompanyAPI_company + id;

        return this.requestService.getJSONObjectRequest(resource, getToken());

    }

    @PostMapping
    public String createCompany(@RequestBody JSONObject jsonObject) throws IOException {

        JSONObject companyPostObject = createCompanyPostObject(jsonObject);

        HttpEntity<String> companyHeader = this.requestService.createCompanyHeader(getToken(), companyPostObject);

        ResponseEntity<String> stringResponseEntity = this.requestService.performPostRequest(CompanyAPI_company, companyHeader);

        return stringResponseEntity.getStatusCode().toString();
    }

    @GetMapping
    public JSONArray getAllCompanies() throws IOException, ParseException {
        return requestService.getJSONArrayRequest(CompanyAPI_company, getToken());
    }

    @GetMapping(value = "/internship_overview_table")
    public JSONArray getDataForInternshipTable() throws IOException, ParseException {

        JSONArray jsonArray = new JSONArray();

        JSONArray allInternships = getAllInternships();

        for (Object jsonObject: allInternships) {

            String company_id = ((JSONObject) jsonObject).get(COMPANY_ID).toString();
            JSONObject company = getCompanyById(company_id);

            ((JSONObject) jsonObject).put(COMPANY_NAME, company.get(COMPANY_NAME));

            jsonArray.add((JSONObject)jsonObject);
        }
        return jsonArray;
    }

    private JSONObject createInternshipPostObject(HashMap<String, String> hashMap) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(COMPANY_ID, hashMap.get(COMPANY_ID));
        jsonObject.put("title", hashMap.get("title"));
        jsonObject.put("description", hashMap.get("description"));
        jsonObject.put("start_date", hashMap.get("start_date")); // yyyy-mm-dd
        jsonObject.put("end_date", hashMap.get("end_date"));
        jsonObject.put("training_type", hashMap.get("internship_type"));

        String study_specialization = hashMap.get("study_specialization");
        JSONParser jsonParser = new JSONParser();
        JSONArray parse = (JSONArray) jsonParser.parse(study_specialization);

        jsonObject.put("study_type", parse);

        return jsonObject;
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

    public JSONObject createCompanyPostObject(JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        result.put(COMPANY_NAME, jsonObject.get(COMPANY_NAME));
        result.put("address", jsonObject.get("address"));
        result.put("zipcode", jsonObject.get("zipcode"));
        result.put("city", jsonObject.get("city"));
        result.put("website", jsonObject.get("website"));

        Object contact_person = jsonObject.get("contact_person");

        result.put("contact_person", contact_person);

        return result;
    }

}
