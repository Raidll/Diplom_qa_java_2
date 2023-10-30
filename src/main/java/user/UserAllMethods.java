package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static baseURL.Specification.spec;

public class UserAllMethods {
    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String CHANGE_USER_DATA = "/api/auth/user";
    private static final String DELETE_USER = "/api/auth/user";

    @Step("Send POST request to /api/auth/register")
    public Response createUser(User user){
        return spec()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(UserEmailAndPasswordModel userEmailAndPasswordModel){
        return spec()
                .header("Content-type", "application/json")
                .body(userEmailAndPasswordModel)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUserWithEmailAndPassword(UserEmailAndPasswordModel userEmailAndPasswordModel){
        return spec()
                .header("Content-type", "application/json")
                .body(userEmailAndPasswordModel)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Send POST request to /api/auth/user")
    public Response changeUserData(UserEmailAndNameModel userEmailAndNameModel, String accessToken){
        return spec()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .body(userEmailAndNameModel)
                .when()
                .patch(CHANGE_USER_DATA);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String accessToken) {
        return spec()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .delete();
    }
}
