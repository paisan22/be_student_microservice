package nl.ipsenh.student.API;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

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
    public HashMap<String, String> createProposal(@RequestBody HashMap<String, String> hashMap) {

        System.out.println("request: " + hashMap);

        String student_email = hashMap.get("student_email");
        String mentor_id = hashMap.get("mentor_id");
        String proposal = hashMap.get("proposal");
        String stage_id = hashMap.get("stage_id");

        HashMap<String, String> statusHashmap = checkInternshipProposal(hashMap);

        JSONObject jsonObject = new JSONObject();
        JSONObject begeleiderObject = new JSONObject();
        begeleiderObject.put("id", mentor_id);

        jsonObject.put("studenten_email", student_email);

        jsonObject.put("begeleiders", begeleiderObject);
        jsonObject.put("stagevoorstel", proposal);
        jsonObject.put("stage_id", stage_id);

        System.out.println(jsonObject);

        return statusHashmap;
    }

    public HashMap<String, String> checkInternshipProposal(HashMap<String, String> hashMap) {

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("status", "true");
        stringStringHashMap.put("student_email","true");
        stringStringHashMap.put("mentor_id","true");
        stringStringHashMap.put("proposal","true");
        stringStringHashMap.put("stage_id","true");

        if (hashMap.size() != 4) {
            stringStringHashMap.put("status", "false");
            stringStringHashMap.put("message", "not all needed keys are available");
        }

        if (hashMap.get("student_email").isEmpty()) {
            stringStringHashMap.put("status", "false");
            stringStringHashMap.put("student_email","false");
        }

        if (hashMap.get("mentor_id").isEmpty()) {
            stringStringHashMap.put("status", "false");
            stringStringHashMap.put("mentor_id","false");

        }

        if (hashMap.get("proposal").isEmpty()) {
            stringStringHashMap.put("status", "false");
            stringStringHashMap.put("proposal","false");

        }

        if (hashMap.get("stage_id").isEmpty()) {
            stringStringHashMap.put("status", "false");
            stringStringHashMap.put("stage_id","false");

        }

        return stringStringHashMap;

    }

    @GetMapping
    public JSONArray getAllDocents() {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "Alex van Manen");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "456");
        jsonObject1.put("name", "Roland Westveer");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id", "456");
        jsonObject2.put("name", "Michiel Boerre");

        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);

        return jsonArray;
    }

    @GetMapping(value = "/email")
    public JSONObject getDocentByEmail(@RequestHeader HashMap<String, String> hashMap) {
        return null;
    }


    //    @GetMapping
//    public JSONArray getAllDocents() {
//
//        String resource = docentAPI + "docenten/api/list";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);
//
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> exchange = restTemplate.exchange(resource, HttpMethod.GET, parameters, String.class);
//
//            String jsonString = exchange.getBody();
//            JSONParser jsonParser = new JSONParser();
//            Object parse = jsonParser.parse(jsonString);
//
//            return (JSONArray) parse;
//
//        } catch (HttpClientErrorException e) {
//            return null;
//        }
//        catch (HttpServerErrorException e) {
//            System.out.println(e.getMessage());
//            JSONArray jsonArray = new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("error", e.getMessage());
//            jsonArray.add(jsonObject);
//            return jsonArray;
//        }
//        catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    return null;
//    }
}
