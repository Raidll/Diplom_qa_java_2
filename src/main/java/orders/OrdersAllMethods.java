package orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.Ingredient;

import static baseURL.Specification.spec;
import static io.restassured.RestAssured.given;

public class OrdersAllMethods {
    private static final String CREATE_ORDER = "/api/orders";
    private static final  String GET_ALL_USER_ORDERS = "/api/orders";

    @Step("Send POST request to /api/orders")
    public Response createOrder(Ingredient ingredient, String accessToken){
        return spec()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .body(ingredient)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("Send POST request to /api/orders/all")
    public Response getAllOrders(String accessToken){
        return spec()
                .contentType("application/json")
                .auth().oauth2(accessToken)
                .when()
                .get(GET_ALL_USER_ORDERS);
    }
}
