package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserAllMethods {
    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String CHANGE_USER_DATA = "/api/auth/user";
    private static final String DELETE_USER = "/api/auth/user";

    @Step("Send POST request to /api/auth/register")
    public Response createUser(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(User user){
        String loginAndPasswordJson = "{\n" +
                "\"email\": " + "\""+ user.getEmail() + "\"" +",\n" +
                "\"password\": " + "\"" + user.getPassword() + "\"\n" +
                "}";

        return given()
                .header("Content-type", "application/json")
                .body(loginAndPasswordJson)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUserWithEmailAndPassword(String email, String password){
        String loginAndPasswordJson = "{\n" +
                "\"email\": " + "\""+ email + "\"" +",\n" +
                "\"password\": " + "\"" + password + "\"\n" +
                "}";

        return given()
                .header("Content-type", "application/json")
                .body(loginAndPasswordJson)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Send POST request to /api/auth/user")
    public Response changeUserData(String email, String name, String accessToken){
        String emailAndPasswordJson = "{\n" +
                "\"email\": " + "\""+ email + "\"" +",\n" +
                "\"name\": " + "\"" + name + "\"\n" +
                "}";

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .body(emailAndPasswordJson)
                .when()
                .patch(CHANGE_USER_DATA);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .delete();
    }
}
