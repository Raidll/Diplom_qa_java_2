package baseURL;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Specification {
    public static RequestSpecification spec(){
        return given()
                .baseUri(BaseURL.getBaseURL());
    }
}
