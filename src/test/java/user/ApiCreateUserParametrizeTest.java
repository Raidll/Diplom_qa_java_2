package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import random.RandomString;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ApiCreateUserParametrizeTest {
    private String email;
    private String password;
    private String name;

    private UserAllMethods userAllMethods = new UserAllMethods();

    public ApiCreateUserParametrizeTest(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> testBadData() {
        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);

        return Arrays.asList(new Object[][]{
                {email, password, ""},
                {email, "", name},
                {"", password, name}
        });
    }

    @Test
    @DisplayName("Check status code when request with bad parameters")
    public void createUserWithBadParameters(){
        User user = new User(email, password, name);
        Response createCourierWithBadParametersResponse = userAllMethods.createUser(user);
        createCourierWithBadParametersResponse
                .then()
                .statusCode(SC_FORBIDDEN);
        Assert.assertEquals(false, createCourierWithBadParametersResponse.path("success"));
        Assert.assertEquals("Email, password and name are required fields", createCourierWithBadParametersResponse.path("message"));
    }

}
