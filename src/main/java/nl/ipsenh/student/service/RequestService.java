package nl.ipsenh.student.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by paisanrietbroek on 18/12/2017.
 */

@Service
public class RequestService {

    public JSONArray getJSONArrayRequest(String resource) throws ParseException {

        HttpEntity<String> headers = createHeaders();

        try {

            ResponseEntity<String> stringResponseEntity = performGetRequest(resource, headers);
            return parseResponseToJSONArray(stringResponseEntity);
        }
        catch (HttpServerErrorException e) {
            return createErrorArray(e);
        }
    }

    public JSONArray getJSONArrayRequest(String resource, String token) throws ParseException {

        HttpEntity<String> headers = createCompanyHeader(token);

        try {
            ResponseEntity<String> stringResponseEntity = performGetRequest(resource, headers);
            return parseResponseToJSONArray(stringResponseEntity);
        }
        catch (HttpServerErrorException e) {
            return createErrorArray(e);
        }
    }

    public JSONObject getJSONObjectRequest(String resource, String token) throws ParseException {
        HttpEntity<String> companyHeader = createCompanyHeader(token);

        try {
            ResponseEntity<String> stringResponseEntity = performGetRequest(resource, companyHeader);
            return parseResponseToJSONObject(stringResponseEntity);
        } catch (HttpServerErrorException e) {
            return createErrorObject(e);
        }
    }


    public HttpEntity<String> createHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);

        return parameters;
    }

    public HttpEntity<String> createCompanyHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", token);

        return new HttpEntity<>("parameters", httpHeaders);
    }

    public HttpEntity<String> createCompanyHeader(String token, JSONObject jsonObject) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", token);

        return new HttpEntity<String>(jsonObject.toJSONString(), httpHeaders);
    }

    public ResponseEntity<String> performGetRequest(String resource, HttpEntity<String> headers) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(resource, HttpMethod.GET, headers, String.class);
    }

    public ResponseEntity<String> performPostRequest(String resource, HttpEntity<String> headers) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(resource, headers, String.class);
    }

    public JSONArray parseResponseToJSONArray(ResponseEntity<String> response) throws ParseException {
        String jsonString = response.getBody();
        JSONParser jsonParser = new JSONParser();
        Object parse = jsonParser.parse(jsonString);

        return (JSONArray) parse;
    }

    public JSONObject parseResponseToJSONObject(ResponseEntity<String> response) throws ParseException {
        String body = response.getBody();
        JSONParser jsonParser = new JSONParser();
        Object parse = jsonParser.parse(body);

        return (JSONObject) parse;
    }

    public JSONArray createErrorArray(Exception e) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", e.getMessage());
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public JSONObject createErrorObject(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", e.getMessage());
        return jsonObject;
    }

}
