package resources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {

	public static RequestSpecification req;

	public RequestSpecification requestSpecification(String apikey, String keyvalue) throws IOException {
		PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
		req = new RequestSpecBuilder().setBaseUri(getGlobalValue("BaseUrI")).addQueryParam(apikey, keyvalue)
				.addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
				.build();
		return req;
	}

	public static String getGlobalValue(String Key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\java\\resources\\global.properties");
		prop.load(fis);
		return prop.getProperty(Key);

	}

	public JsonPath getJsonPath(Response response) {
		JsonPath js = new JsonPath(response.asString());
		return js;

	}

}
