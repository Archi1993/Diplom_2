package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import specifications.Client;


public class OrderClient extends Client {
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";
    private static final String allCorrectIngredient = "{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa73\"]\n}";
    private static final String allIncorrectIngredient = "{\n\"ingredients\": [\"ffffffa71d1f82001bdaaa6d\",\"91c0c5a444f82001bdaaa70\",\"91c0c5a333f82001bdaaa73\"]\n}";


    @Step("Создание заказа авторизированного пользователя")
    public ValidatableResponse createOrderAuthorizedUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(allCorrectIngredient)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа неавторизированного пользователя")
    public ValidatableResponse createOrderUnAuthorizedUser() {
        return spec()
                .body(allCorrectIngredient)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа c неверным хешем ингредиентов")
    public ValidatableResponse createOrderWithUnknownIngredients(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .body(allIncorrectIngredient)
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
