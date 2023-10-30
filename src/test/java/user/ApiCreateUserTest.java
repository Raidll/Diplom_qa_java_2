package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import random.RandomString;
import static org.apache.http.HttpStatus.*;

public class ApiCreateUserTest {
    private UserAllMethods userAllMethods = new UserAllMethods();
    private String accessToken;


    @Test
    @DisplayName("Check response /api/auth/register")
    public void createUniqueUserTest(){
        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);

        User user = new User(email, password, name);
        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        accessToken = fullAccessToken.substring(7);
        createUserResponse.
                then().
                assertThat().
                statusCode(SC_OK);
        Assert.assertTrue( createUserResponse.path("success"));
        Assert.assertEquals(email, createUserResponse.path("user.email"));
        Assert.assertEquals(name, createUserResponse.path("user.name"));
    }

    @Test
    @DisplayName("Check response /api/auth/register")
    public void createNotUniqueUserTest(){
        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);

        User user = new User(email, password, name);
        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        accessToken = fullAccessToken.substring(7);
        Response createNotUniqueUser = userAllMethods.createUser(user);
        createNotUniqueUser.
                then().
                assertThat().
                statusCode(SC_FORBIDDEN);
        Assert.assertEquals(false, createNotUniqueUser.path("success"));
        Assert.assertEquals("User already exists", createNotUniqueUser.path("message"));
    }

    @After
    public void deleteUser(){
        if (accessToken != null) {
            userAllMethods.deleteUser(this.accessToken);
        }
    }

}
