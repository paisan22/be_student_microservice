package nl.ipsenh.student.API;

import nl.ipsenh.student.service.RequestService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by paisanrietbroek on 12/12/2017.
 */

@RestController
@RequestMapping(value = "/docent")
@CrossOrigin(value = "*")
public class DocentAPI {

    @Autowired
    private RequestService requestService;

    private final String docentAPI = "http://ipsenh.win/";

    @PostMapping(value = "/internship_proposal")
    public JSONObject createProposal(@RequestBody HashMap<String, String> hashMap) {

        String resource = docentAPI + "stagevoorstellen/api/listcreate/";

        System.out.println("request: " + hashMap);

        String student_email = hashMap.get("student_email");
        String mentor_id = hashMap.get("mentor_id");
        String proposal = hashMap.get("proposal");
        String stage_id = hashMap.get("stage_id");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("student_email", student_email);

        JSONObject begeleiders = new JSONObject();
        begeleiders.put("id", mentor_id);
        begeleiders.put("rol", "mentor");

        jsonObject.put("begeleiders", begeleiders);

        jsonObject.put("stagevoorstel", proposal);
        jsonObject.put("stage_id", stage_id);

        JSONObject status = new JSONObject();
        status.put("text", "In behandeling door mentor");
        status.put("percentage", "10");
        jsonObject.put("status", status);

//        HttpEntity<String> headers = requestService.createHeaders();
//        ResponseEntity<String> stringResponseEntity = requestService.performPostRequest(resource, headers);

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

    @GetMapping(value = "/slb")
    public JSONObject getDocentByEmail(@RequestHeader HashMap<String, String> hashMap) {

        String slb_email = hashMap.get("slb_email");

        JSONArray allDocents = getAllDocents();

        for (int i = 0; i < allDocents.size(); i++) {

            JSONObject o = (JSONObject) allDocents.get(i);
            boolean slbEmail = o.get("slbEmail").equals(slb_email);

            System.out.println(o.get("slbEmail"));

            if (slbEmail) {
                return o;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "not found");

        return jsonObject;

    }

//    @GetMapping(value = "/email")
//    public JSONObject getDocentByEmail(@RequestHeader HashMap<String, String> hashMap) {
//        return null;
//    }


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
