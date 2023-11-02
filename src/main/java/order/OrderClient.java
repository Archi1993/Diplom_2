package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import specifications.Client;

import java.util.ArrayList;
import java.util.List;


public class OrderClient extends Client {
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";
    private static final String FIRST_INGREDIENT = "61c0c5a71d1f82001bdaaa6d";
    private static final String SECOND_INGREDIENT = "61c0c5a71d1f82001bdaaa70";
    private static final String THIRD_INGREDIENT = "61c0c5a71d1f82001bdaaa73";
    private static final String ALL_CORRECT_INGREDIENT = "{\n\"ingredients\": [\"" + FIRST_INGREDIENT + "\",\"" + SECOND_INGREDIENT + "\",\"" + THIRD_INGREDIENT + "\"]\n}";
    private static final String ALL_INCORRECT_INGREDIENT = "{\n\"ingredients\": [\"" + FIRST_INGREDIENT + "test1\",\"" + SECOND_INGREDIENT + "test2\",\"" + THIRD_INGREDIENT + "test3\"]\n}";


    @Step("Создание заказа авторизированного пользователя")
    public ValidatableResponse createOrderAuthorizedUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(ALL_CORRECT_INGREDIENT)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа неавторизированного пользователя")
    public ValidatableResponse createOrderUnAuthorizedUser() {
        return spec()
                .body(ALL_CORRECT_INGREDIENT)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа c неверным хешем ингредиентов")
    public ValidatableResponse createOrderWithUnknownIngredients(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .body(ALL_INCORRECT_INGREDIENT)
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
