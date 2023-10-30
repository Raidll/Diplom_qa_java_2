package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import random.RandomString;
import static org.apache.http.HttpStatus.*;

public class ApiLoginUserTest {

    private UserAllMethods userAllMethods = new UserAllMethods();
    private String accessToken;

    @Test
    @DisplayName("Check response /api/auth/login")
    public void loginUserSuccessTest(){
        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);

        User user = new User(email, password, name);
        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        accessToken = fullAccessToken.substring(7);
        Response successLoginResponse = userAllMethods.loginUser(new UserEmailAndPasswordModel(user.getEmail(), user.getPassword()));
        successLoginResponse
                .then()
                .statusCode(SC_OK);
        Assert.assertEquals(true, successLoginResponse.path("success"));
        Assert.assertEquals(email, successLoginResponse.path("user.email"));
        Assert.assertEquals(name, successLoginResponse.path("user.name"));
    }

    @After
    public void deleteUser(){
        if (accessToken != null) {
            userAllMethods.deleteUser(this.accessToken);
        }
    }

}
