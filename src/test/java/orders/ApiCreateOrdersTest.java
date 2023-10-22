package user;

import baseURL.BaseURL;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import random.RandomString;
import static org.apache.http.HttpStatus.*;


import java.util.ArrayList;
import java.util.List;

public class ApiCreateOrdersTest {
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
    @DisplayName("Check response /api/orders")
    public void createOrderWithThreeIngredientsAuthorisationSuccess(){
        List<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add("61c0c5a71d1f82001bdaaa75");
        ingredientsList.add("61c0c5a71d1f82001bdaaa70");
        ingredientsList.add("61c0c5a71d1f82001bdaaa6c");
        Ingredient ingredients = new Ingredient(ingredientsList);

        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        String accessToken = fullAccessToken.substring(7);

        Response createOrderResponse = userAllMethods.createOrder(ingredients, accessToken);
        createOrderResponse.then().statusCode(SC_OK);
        Assert.assertEquals(true, createOrderResponse.path("success"));
    }

    @Test
    @DisplayName("Check response /api/orders")
    public void createOrderWithoutAuthorisationError(){
        String randomString = RandomString.generateRandomHexString(5);

        List<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add("61c0c5a71d1f82001bdaaa75");
        ingredientsList.add("61c0c5a71d1f82001bdaaa70");
        ingredientsList.add("61c0c5a71d1f82001bdaaa6c");
        Ingredient ingredients = new Ingredient(ingredientsList);

        Response createOrderResponse = userAllMethods.createOrder(ingredients, randomString);
        createOrderResponse.then().statusCode(SC_FORBIDDEN);
        Assert.assertEquals(false, createOrderResponse.path("success"));
        Assert.assertEquals("invalid token", createOrderResponse.path("message"));
    }

    @Test
    @DisplayName("Check response /api/orders")
    public void createOrderWithInvalidIngredientHashError(){
        String randomString = RandomString.generateRandomHexString(5);

        List<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add(randomString);
        Ingredient ingredients = new Ingredient(ingredientsList);

        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        String accessToken = fullAccessToken.substring(7);

        Response createOrderResponse = userAllMethods.createOrder(ingredients, accessToken);
        createOrderResponse.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Check response /api/orders")
    public void createOrderWithoutIngredientsError(){
        List<String> ingredientsList = new ArrayList<String>();
        ingredientsList.add("");
        Ingredient ingredients = new Ingredient(ingredientsList);

        Response createUserResponse = userAllMethods.createUser(user);
        String fullAccessToken = createUserResponse.path("accessToken");
        String accessToken = fullAccessToken.substring(7);

        Response createOrderResponse = userAllMethods.createOrder(ingredients, accessToken);
        createOrderResponse.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
