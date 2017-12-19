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
    public JSONObject createProposal(@RequestBody HashMap<String, String> hashMap) {

        String resource = docentAPI + "stagevoorstellen/api/listcreate/";

        System.out.println("request: " + hashMap);

        String student_email = hashMap.get("student_email");
        String mentor_id = hashMap.get("mentor_id");
        String proposal = hashMap.get("proposal");
        String stage_id = hashMap.get("stage_id");

        JSONObject jsonObjectCheck = checkInternshipProposal(hashMap);

        if (jsonObjectCheck.get("status").toString() == "true") {

//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> parameters = new HttpEntity<>("parameters", httpHeaders);
//
//            try {
//                RestTemplate restTemplate = new RestTemplate();
//                ResponseEntity<String> exchange = restTemplate.exchange(resource, HttpMethod.POST, parameters, String.class);
//
//                String jsonString = exchange.getBody();
//                JSONParser jsonParser = new JSONParser();
//
//            } catch (HttpClientErrorException e) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("status", "false");
//                jsonObject.put("message", "docentAPI connection error");
//                return jsonObject;
//            }
//            catch (HttpServerErrorException e) {
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("status", "false");
//                jsonObject.put("message", "docentAPI connection error");
//                return jsonObject;
//            }
//
//            JSONObject jsonObject = new JSONObject();
//            JSONObject begeleiderObject = new JSONObject();
//            begeleiderObject.put("id", mentor_id);
//            begeleiderObject.put("rol", "mentor");
//
//            jsonObject.put("studenten_email", student_email);
//
//            jsonObject.put("begeleiders", begeleiderObject);
//            jsonObject.put("stagevoorstel", proposal);
//            jsonObject.put("stage_id", stage_id);
//
//
//
//            return jsonObject;

            return jsonObjectCheck;
        }

        return jsonObjectCheck;
    }

    public JSONObject checkInternshipProposal(HashMap<String, String> hashMap) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("status", "true");
        jsonObject.put("student_email","true");
        jsonObject.put("mentor_id","true");
        jsonObject.put("proposal","true");
        jsonObject.put("stage_id","true");

        if (hashMap.size() != 4) {
            jsonObject.put("status", "false");
            jsonObject.put("message", "not all needed keys are available");
        }

        if (hashMap.get("student_email").isEmpty()) {
            jsonObject.put("status", "false");
            jsonObject.put("student_email","false");
        }

        if (hashMap.get("mentor_id").isEmpty()) {
            jsonObject.put("status", "false");
            jsonObject.put("mentor_id","false");

        }

        if (hashMap.get("proposal").isEmpty()) {
            jsonObject.put("status", "false");
            jsonObject.put("proposal","false");

        }

        if (hashMap.get("stage_id").isEmpty()) {
            jsonObject.put("status", "false");
            jsonObject.put("stage_id","false");

        }

        return jsonObject;

    }

    @GetMapping
    public JSONArray getAllDocents() {

        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "Alex van Manen");
        jsonObject.put("slbEmail", "alex@mail.nl");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "456");
        jsonObject1.put("name", "Roland Westveer");
        jsonObject1.put("slbEmail", "roland@mail.nl");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id", "456");
        jsonObject2.put("name", "Michiel Boerre");
        jsonObject2.put("slbEmail", "michiel@mail.nl");

        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);

        return jsonArray;
    }

    @GetMapping(value = "/{email}")
    public JSONObject getDocentByEmail(@PathVariable String email) {

        JSONArray allDocents = getAllDocents();

        for (int i = 0; i < allDocents.size(); i++) {
            JSONObject o = (JSONObject) allDocents.get(i);
            boolean slbEmail = o.get("slbEmail") == email;

            if (slbEmail) {
                return o;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "not found");
        return jsonObject;

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
