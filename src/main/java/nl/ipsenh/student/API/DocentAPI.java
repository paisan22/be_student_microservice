package nl.ipsenh.student.API;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by paisanrietbroek on 12/12/2017.
 */

@RestController
@RequestMapping(value = "/docent")
@CrossOrigin(value = "*")
public class DocentAPI {

    private final String docentAPI = "http://ipsenh.win/";

    @PostMapping(value = "/internship_proposal")
    public JSONObject createProposal(@RequestBody HashMap<String, String> hashMap) {

        System.out.println("request: " + hashMap);

        String student_email = hashMap.get("student_email");
        String mentor_id = hashMap.get("mentor_id");
        String proposal = hashMap.get("proposal");

        JSONObject jsonObject = new JSONObject();
        JSONObject begeleiderObject = new JSONObject();
        begeleiderObject.put("id", mentor_id);

        jsonObject.put("studenten_email", student_email);

        jsonObject.put("begeleiders", begeleiderObject);
        jsonObject.put("stagevoorstel", proposal);

        return jsonObject;
    }

    @GetMapping
    public JSONArray getAllDocents() {

        String resource = docentAPI + "docenten/api/list";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> exchange = restTemplate.exchange(resource, HttpMethod.GET, parameters, String.class);

            String jsonString = exchange.getBody();
            JSONParser jsonParser = new JSONParser();
            Object parse = jsonParser.parse(jsonString);

            return (JSONArray) parse;

        } catch (HttpClientErrorException e) {
            return null;
        }
        catch (HttpServerErrorException e) {
            System.out.println(e.getMessage());
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", e.getMessage());
            jsonArray.add(jsonObject);
            return jsonArray;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

    return null;
    }
}
