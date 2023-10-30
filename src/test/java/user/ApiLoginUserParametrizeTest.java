package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import random.RandomString;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ApiLoginUserParametrizeTest {
    private String email;
    private String password;
    private User user;


    private UserAllMethods userAllMethods = new UserAllMethods();

    public ApiLoginUserParametrizeTest(String email, String password, User user){
        this.email = email;
        this.password = password;
        this.user = user;


    }


    @Parameterized.Parameters
    public static Collection<Object[]> testBadData() {
        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);
        String randomString = RandomString.generateRandomHexString(5);

        User user = new User(email, password, name);

        return Arrays.asList(new Object[][]{
                {email, "", user},
                {email, randomString, user},
                {"", password, user},
                {randomString, password, user}
        });
    }

    @Test
    @DisplayName("Check status code when request with bad parameters")
    public void loginUserWithBadParameters(){
        userAllMethods.createUser(user);
        Response loginUserWithBadParameterResponse = userAllMethods.loginUserWithEmailAndPassword(new UserEmailAndPasswordModel(email, password));
        loginUserWithBadParameterResponse
                .then()
                .statusCode(SC_UNAUTHORIZED);
        Assert.assertEquals(false, loginUserWithBadParameterResponse.path("success"));
        Assert.assertEquals("email or password are incorrect", loginUserWithBadParameterResponse.path("message"));
    }


}
