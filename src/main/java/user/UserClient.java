package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import specifications.Client;


public class UserClient extends Client {
    private static final String CREATE_NEW_USER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_USER_ENDPOINT = "/api/auth/login";
    private static final String UPDATE_OR_DELETE_USER_ENDPOINT = "/api/auth/user";
    private static final String LOGOUT_USER_ENDPOINT = "/api/auth/logout";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(CREATE_NEW_USER_ENDPOINT)
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(Credentials creds) {
        return spec()
                .body(creds)
                .when()
                .post(LOGIN_USER_ENDPOINT)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .delete(UPDATE_OR_DELETE_USER_ENDPOINT)
                .then().log().all();
    }

    @Step("Обновление пользователя")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(UPDATE_OR_DELETE_USER_ENDPOINT)
                .then().log().all();
    }

    @Step("Получение данных пользователя")
    public ValidatableResponse getUserInfo(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(UPDATE_OR_DELETE_USER_ENDPOINT)
                .then().log().all();
    }

    @Step("Выход пользователя из системы")
    public ValidatableResponse logoutUser(String refreshToken) {
        return spec()
                .body("{\"token\": " + "\"" + refreshToken + "\"" + "}")
                .when()
                .post(LOGOUT_USER_ENDPOINT)
                .then().log().all();
    }

}
