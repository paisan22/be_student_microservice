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

    private static final String RESOURCE_DOCENT_API = "http://ipsenh.win/";

    private static final String STAGEVOORSTEL = "stagevoorstel";
    private static final String PERCENTAGE = "percentage";
    private static final String SLB_EMAIL = "slbEmail";

    @PostMapping(value = "/internship_proposal")
    public JSONObject createProposal(@RequestBody HashMap<String, String> hashMap) {

        String resource = RESOURCE_DOCENT_API + "stagevoorstellen/api/listcreate/"; // can be use when the docentAPI is ready

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

        jsonObject.put(STAGEVOORSTEL, proposal);
        jsonObject.put("stage_id", stage_id);

        JSONObject status = new JSONObject();
        status.put("text", "In behandeling door mentor");
        status.put(PERCENTAGE, "20");
        jsonObject.put("status", status);

        return jsonObject;
    }

    @GetMapping
    public JSONArray getAllDocents() {

        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "Alex van Manen");
        jsonObject.put(SLB_EMAIL, "alex@mail.nl");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "456");
        jsonObject1.put("name", "Roland Westveer");
        jsonObject1.put(SLB_EMAIL, "roland@mail.nl");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id", "456");
        jsonObject2.put("name", "Michiel Boerre");
        jsonObject2.put(SLB_EMAIL, "michiel@mail.nl");

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
            boolean slbEmail = o.get(SLB_EMAIL).equals(slb_email);

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

        String email = (String) hashMap.get("email");

        JSONObject jsonObject = new JSONObject();
        JSONObject status = new JSONObject();
        status.put(PERCENTAGE, 0);

        jsonObject.put("internship_description", "Bedrijfsbeschrijving");
        jsonObject.put("company_name", "bedrijfsnaam");

        // email van mike
        if (Objects.equals(email, "s1098641@student.hsleiden.nl")) {
            jsonObject.put(STAGEVOORSTEL, "test voorstel beschrijving van mike");

            status.put("text", "Aanpassingen van de student vereist");
            status.put(PERCENTAGE, 12);

        }

        // email van gerben
        if (Objects.equals(email, "s1085142@student.hsleiden.nl")) {
            jsonObject.put(STAGEVOORSTEL, "test voorstel beschrijving van gerben");

            status.put("text", "Stagevoorstel goedgekeurd! Je bent klaar om stage te lopen.");
            status.put(PERCENTAGE, 100);
        }

        jsonObject.put("status", status);

        return jsonObject;
    }
}
