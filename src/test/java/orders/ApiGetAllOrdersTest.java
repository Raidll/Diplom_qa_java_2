package orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import random.RandomString;
import user.Ingredient;
import user.User;
import user.UserAllMethods;

import static org.apache.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;

public class ApiGetAllOrdersTest {
    private UserAllMethods userAllMethods = new UserAllMethods();
    private OrdersAllMethods ordersAllMethods = new OrdersAllMethods();
    private User user;
    private String accessToken;

    @Before
    public  void setUp() {
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
        accessToken = fullAccessToken.substring(7);

        ordersAllMethods.createOrder(ingredients, accessToken);
        Response getAllOrdersResponse = ordersAllMethods.getAllOrders(accessToken);
        getAllOrdersResponse.then().statusCode(SC_OK);
        Assert.assertEquals(true, getAllOrdersResponse.path("success"));
        Assert.assertEquals(ingredientsList, getAllOrdersResponse.path("orders[0].ingredients"));
    }

    @Test
    @DisplayName("Check response /api/orders/all")
    public void getAllUserOrdersWithInvalidTokenError(){
        String randomString = RandomString.generateRandomHexString(5);
        Response getAllOrdersResponse = ordersAllMethods.getAllOrders(randomString);
        getAllOrdersResponse.then().statusCode(SC_FORBIDDEN);
        Assert.assertFalse(getAllOrdersResponse.path("success"));
        Assert.assertEquals("jwt malformed", getAllOrdersResponse.path("message"));
    }

    @After
    public void deleteUser(){
        if (accessToken != null) {
            userAllMethods.deleteUser(this.accessToken);
        }
    }
}
