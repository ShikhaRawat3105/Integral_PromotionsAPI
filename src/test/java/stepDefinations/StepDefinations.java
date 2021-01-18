package stepDefinations;

import static io.restassured.RestAssured.given;
import java.io.IOException;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.Utils;
import static org.junit.Assert.*;

public class StepDefinations extends Utils {

	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;

	@Given("GetPromotions payload with {string} as {string}")
	public void get_promotions_payload_with_as(String apikey, String keyvalue) throws IOException {

		res = given().spec(requestSpecification(apikey, keyvalue));
	}

	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String HttpMethod) {

		APIResources resourceAPI = APIResources.valueOf(resource);
		resspec = new ResponseSpecBuilder().expectContentType(ContentType.JSON).build();
		if (HttpMethod.equalsIgnoreCase("Get"))
			response = res.when().get(resourceAPI.getResource()).then().log().all().spec(resspec).extract().response();

	}

	@Then("Response HTTP Status code should be {string}")
	public void response_http_status_code_should_be(String scode) {

		assertEquals(response.getStatusCode(), Integer.parseInt(scode));

	}

	@Then("{string} in response body should exist")
	public void in_response_body_should_exist(String keyValue) {

		JSONObject obj = new JSONObject(response.asString());
		if (keyValue.equalsIgnoreCase("promotions")) {
			assertTrue(obj.has("promotions"));
		} else {
			JSONArray arr = obj.getJSONArray("promotions");
			for (int i = 0; i < arr.length(); i++) {
				if (!(arr.getJSONObject(i).has(keyValue)))
					assertTrue(false);
			}
			assertTrue(true);
		}
	}

	@Then("{string} should have value as True or false")
	public void should_have_value_as_true_or_false(String keyValue) {

		JsonPath responseJsonPath = getJsonPath(response);
		for (int i = 0; i < responseJsonPath.getInt("promotions.size()"); i++) {
			Boolean showPrice = responseJsonPath.get("promotions[" + i + "]." + keyValue);
			if (!(showPrice == true || showPrice == false)) {
				assertTrue(false);
			}
		}
		assertTrue(true);
	}

	@Then("{string} should exist with “ar” and “en” json objects")
	public void should_exist_with_ar_and_en_json_objects(String localizedTexts) {

		JSONObject obj = new JSONObject(response.asString());
		JSONArray arr = obj.getJSONArray("promotions");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject localizedTextsObj = arr.getJSONObject(i).getJSONObject(localizedTexts);
			if (!(localizedTextsObj.has("ar") && localizedTextsObj.has("en")))
				assertTrue(false);
		}
		assertTrue(true);
	}

	@Then("{string} should have any string value")
	public void should_have_any_string_value(String promotionId) {

		JsonPath js = getJsonPath(response);
		int size = js.getInt("promotions.size()");
		for (int i = 0; i < size; i++) {
			if (!(js.get("promotions[" + i + "]." + promotionId + "").getClass().getSimpleName()).equals("String")) {
				assertTrue(false);
			}
		}
		assertTrue(true);

	}

	@Then("{string} have value as as EPISODE or MOVIE or SERIES or SEASON")
	public void have_value_as_as_episode_or_movie_or_series_or_season(String key) {

		JSONObject obj = new JSONObject(response.asString());
		JSONArray arr = obj.getJSONArray("promotions");

		String[] alphabet = new String[] { "episode", "movie", "series", "season" };

		// Convert String Array to List
		java.util.List<String> programList = Arrays.asList(alphabet);

		for (int prom = 0; prom < arr.length(); prom++) {
			JSONArray propArr = arr.getJSONObject(prom).getJSONArray("properties");
			for (int prop = 0; prop < propArr.length(); prop++) {
				String progType = propArr.getJSONObject(prop).getString(key);
				if (!programList.contains(progType))
					assertTrue(false);
			}
		}

		assertTrue(true);
	}

	@Then("{string} should not be null")
	public void should_not_be_null(String requestId) {

		JsonPath js = getJsonPath(response);
		assertTrue(!(js.get("error." + requestId + "").toString().isEmpty()));

	}

	@Then("{string} should be {string}")
	public void should_be(String key, String value) {
		
		JsonPath js = getJsonPath(response);
		assertEquals(js.get("error." + key + "").toString(), value);

	}

}
