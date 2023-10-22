package user;

import baseURL.BaseURL;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import random.RandomString;
import static org.apache.http.HttpStatus.*;

public class ApiChangeUserDataTest {
    private UserAllMethods userAllMethods = new UserAllMethods();
    private User user;
    private String accessToken;

    @Before
    public  void setUp() {
        RestAssured.baseURI = BaseURL.getBaseURL();

        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);

        this.user = new User(email, password, name);
    }

    @Test
    @DisplayName("Check response /api/auth/user")
    public void changeUserEmailWithAuthorisationTest(){
        String newEmail = RandomString.generateRandomHexString(10) + "@mail.ru";

        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        accessToken = fullAccessToken.substring(7);
        Response changeUserEmailResponse = userAllMethods.changeUserData(newEmail, user.getName(), accessToken);
        changeUserEmailResponse.then().statusCode(SC_OK);
        Assert.assertEquals(true, changeUserEmailResponse.path("success"));
        Assert.assertEquals(newEmail, changeUserEmailResponse.path("user.email"));
        Assert.assertEquals(user.getName(),changeUserEmailResponse.path("user.name"));
    }

    @Test
    @DisplayName("Check response /api/auth/user")
    public void changeUserNameWithAuthorisationTest(){
        String newName = RandomString.generateRandomHexString(5);

        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        accessToken = fullAccessToken.substring(7);
        Response changeUserEmailResponse = userAllMethods.changeUserData(user.getEmail(), newName, accessToken);
        changeUserEmailResponse.then().statusCode(SC_OK);
        Assert.assertEquals(true, changeUserEmailResponse.path("success"));
        Assert.assertEquals(user.getEmail(), changeUserEmailResponse.path("user.email"));
        Assert.assertEquals(newName,changeUserEmailResponse.path("user.name"));
    }

    @Test
    @DisplayName("Check response /api/auth/user")
    public void changeUserNameWithoutAuthorisationTest(){
        String newName = RandomString.generateRandomHexString(5);

        userAllMethods.createUser(user);
        Response changeUserEmailResponse = userAllMethods.changeUserData(user.getEmail(), newName, "");
        changeUserEmailResponse.then().statusCode(SC_UNAUTHORIZED);
        Assert.assertEquals(false, changeUserEmailResponse.path("success"));
        Assert.assertEquals("You should be authorised", changeUserEmailResponse.path("message"));
    }

    @Test
    @DisplayName("Check response /api/auth/user")
    public void changeUserEmailWithoutAuthorisationTest(){
        String newEmail = RandomString.generateRandomHexString(5);

        userAllMethods.createUser(user);
        Response changeUserEmailResponse = userAllMethods.changeUserData(newEmail, user.getName(), "");
        changeUserEmailResponse.then().statusCode(SC_UNAUTHORIZED);
        Assert.assertEquals(false, changeUserEmailResponse.path("success"));
        Assert.assertEquals("You should be authorised", changeUserEmailResponse.path("message"));
    }

    @After
    public void deleteUser(){
        if (accessToken != null) {
            userAllMethods.deleteUser(this.accessToken);
        }
    }

}
