package nl.ipsenh.student.API;

import nl.ipsenh.student.service.RequestService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

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
        status.put("percentage", "20");
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

    @GetMapping(value = "/internship")
    public JSONObject getIntenship(@RequestHeader HashMap hashMap) {

        String email = hashMap.get("email").toString();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("internship_description", "Bedrijfsbeschrijving");
        jsonObject.put("company_name", "bedrijfsnaam");

        // email van mike
        if (Objects.equals(email, "s1098641@student.hsleiden.nl")) {
            jsonObject.put("stagevoorstel", "test voorstel beschrijving van mike");


            JSONObject status = new JSONObject();
            status.put("text", "Aanpassingen van de student vereist");
            status.put("percentage", 12);
            jsonObject.put("status", status);
        }

        // email van gerben
        if (Objects.equals(email, "s1085142@student.hsleiden.nl")) {
            jsonObject.put("stagevoorstel", "test voorstel beschrijving van gerben");

            JSONObject status = new JSONObject();
            status.put("text", "Stagevoorstel goedgekeurd! Je bent klaar om stage te lopen.");
            status.put("percentage", 100);
            jsonObject.put("status", status);
        }

        return jsonObject;
    }
}
