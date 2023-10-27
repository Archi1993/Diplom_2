package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import specifications.Client;


public class OrderClient extends Client {
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";


    @Step("Создание заказа авторизированного пользователя")
    public ValidatableResponse createOrderAuthorizedUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa73\"]\n}")
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа неавторизированного пользователя")
    public ValidatableResponse createOrderUnAuthorizedUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa73\"]\n}")
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Создание заказа c неверным хешем ингредиентов")
    public ValidatableResponse createOrderWithUnknownIngredients(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .body("{\n\"ingredients\": [\"ffffffa71d1f82001bdaaa6d\",\"91c0c5a444f82001bdaaa70\",\"91c0c5a333f82001bdaaa73\"]\n}")
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
    public ValidatableResponse getFamousUserOrders(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(GET_ORDER_ENDPOINT)
                .then().log().all();
    }

    @Step("Получение заказа неавторизованного пользователя")
    public ValidatableResponse getUnknownUserOrders(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(GET_ORDER_ENDPOINT)
                .then().log().all();
    }

}
