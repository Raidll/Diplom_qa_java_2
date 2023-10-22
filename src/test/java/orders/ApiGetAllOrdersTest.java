package user;

import baseURL.BaseURL;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import random.RandomString;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;

public class ApiGetAllOrdersTest {
    private UserAllMethods userAllMethods = new UserAllMethods();
    private User user;

    @Before
    public  void setUp() {
        RestAssured.baseURI = BaseURL.getBaseURL();

        String email = RandomString.generateRandomHexString(10) + "@mail.ru";
        String password = RandomString.generateRandomHexString(5);
        String name = RandomString.generateRandomHexString(5);

        this.user = new User(email, password, name);
    }

    @Test
    @DisplayName("Check response /api/orders/all")
    public void getAllOrdersWhenAuthorisationSuccess(){
        List<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add("61c0c5a71d1f82001bdaaa75");
        Ingredient ingredients = new Ingredient(ingredientsList);

        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        String accessToken = fullAccessToken.substring(7);

        userAllMethods.createOrder(ingredients, accessToken);
        Response getAllOrdersResponse = userAllMethods.getAllOrders(accessToken);
        //getAllOrdersResponse.then().body(equalTo("1"));
        getAllOrdersResponse.then().statusCode(SC_OK);
        Assert.assertEquals(true, getAllOrdersResponse.path("success"));
        Assert.assertEquals(ingredientsList, getAllOrdersResponse.path("orders[0].ingredients"));
    }

    @Test
    @DisplayName("Check response /api/orders/all")
    public void getAllUserOrdersWithInvalidTokenError(){
        String randomString = RandomString.generateRandomHexString(5);
        Response getAllOrdersResponse = userAllMethods.getAllOrders(randomString);
        getAllOrdersResponse.then().statusCode(SC_FORBIDDEN);
        Assert.assertEquals(false, getAllOrdersResponse.path("success"));
        Assert.assertEquals("jwt malformed", getAllOrdersResponse.path("message"));

    }
}
