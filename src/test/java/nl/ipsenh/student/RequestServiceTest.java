package nl.ipsenh.student;

import nl.ipsenh.student.service.RequestService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestClientException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isA;

/**
 * Created by paisanrietbroek on 09/01/2018.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class RequestServiceTest {

    @InjectMocks
    private RequestService requestService;

    @Test
    public void testCreateHeaders() throws Exception {

        HttpEntity<String> result = this.requestService.createHeaders();
        HttpHeaders headers = result.getHeaders();

        Assert.assertThat(result, isA(HttpEntity.class));
        Assert.assertThat(headers.getContentType(), isA(MediaType.class));
        Assert.assertThat(headers.getContentType(), is(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateCompanyHeaders() {
        HttpEntity<String> result = this.requestService.createCompanyHeader("abc123");

        HttpHeaders headers = result.getHeaders();

        Assert.assertThat(headers.get("Authorization").get(0), is("abc123"));
        Assert.assertThat(headers.getContentType(), isA(MediaType.class));
        Assert.assertThat(headers.getContentType(), is(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateCompanyHeadersWithJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("test", "this is a test value");

        HttpEntity<String> result = this.requestService.createCompanyHeader("abc123", jsonObject);

        JSONObject body = (JSONObject) JSONValue.parse(result.getBody());

        Assert.assertThat(body.get("test"), is("this is a test value"));
    }

    @Test
    public void testPerformGetRequest() {

        ResponseEntity<String> result = this.requestService.performGetRequest("resource", this.requestService.createHeaders());

        Assert.assertThat(result, isA(ResponseEntity.class));
        Assert.assertThat(result.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testPerformPostRequest() {

        ResponseEntity<String> result = this.requestService.performPostRequest("resource", this.requestService.createHeaders());

        Assert.assertThat(result.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testParseResponseToJSONObject() throws ParseException {

        JSONObject body = new JSONObject();
        body.put("test","test value");

        String bodyString = JSONObject.toJSONString(body);

        ResponseEntity<String> jsonObjectResponseEntity = new ResponseEntity<String>(bodyString, HttpStatus.OK);

        JSONObject result = this.requestService.parseResponseToJSONObject(jsonObjectResponseEntity);

        Assert.assertThat(result.get("test"), is("test value"));
    }

    @Test
    public void testParseResponseToJSONArray() throws ParseException {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("test", "test1 value");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject1.put("test", "test2 value");

        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);

        String bodyString = JSONArray.toJSONString(jsonArray);
        ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(bodyString, HttpStatus.OK);

        JSONArray result = this.requestService.parseResponseToJSONArray(stringResponseEntity);

        Assert.assertThat(result.size(), is(2));
    }

    @Test
    public void testCreateErrorArray() {
        RestClientException restClientException = new RestClientException("test exception");

        JSONArray result = this.requestService.createErrorArray(restClientException);

        JSONObject o = (JSONObject) result.get(0);

        Assert.assertThat(o.get("error"), is("test exception"));
    }

    @Test
    public void testCreateErrorObject() {
        RestClientException restClientException = new RestClientException("test exception");

        JSONObject result = this.requestService.createErrorObject(restClientException);

        Assert.assertThat(result.get("error"), is("test exception"));
    }
}
