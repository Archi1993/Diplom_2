package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import specifications.Client;


public class OrderClient extends Client {
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";


    @Step("Создание заказа авторизированного пользователя")
    public ValidatableResponse createOrderAuthorizedUser(String accessToken, Order order) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа неавторизированного пользователя")
    public ValidatableResponse createOrderUnAuthorizedUser(Order order) {
        return spec()
                .body(order)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа c неверным хешем ингредиентов")
    public ValidatableResponse createOrderWithUnknownIngredients(String accessToken, Order order) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .body(order)
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа без ингридиентов")
    public ValidatableResponse createWithoutIngredients(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Получение заказа авторизованного пользователя")
    public ValidatableResponse getAuthorizedUserOrders(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(GET_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Получение заказа неавторизованного пользователя")
    public ValidatableResponse getUnknownUserOrders() {
        return spec()
                .when()
                .get(GET_ORDER_ENDPOINT)
                .then().log().all();
    }

}
