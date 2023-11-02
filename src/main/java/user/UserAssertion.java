package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;
import java.util.Locale;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserAssertion {
    @Step("Проверка ответа успешного создания пользователя")
    public void createdSuccessfully(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true));
    }

    @Step("Проверка ответа успешной авторизации пользователя")
    public String loggedInSuccessfully(ValidatableResponse loginResponse) {
        return loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("accessToken");
    }

    @Step("Проверка ответа успешного удаления пользователя")
    public void deletedSuccessfully(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_ACCEPTED)
                .body("message", equalTo("User successfully removed"));
    }

    @Step("Проверка ответа при попытке повторного создания существующего пользователя")
    public void createAnExistingUser(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка обновления email")
    public void checkNewEmail(ValidatableResponse response, String newEmail) {
        response
                .assertThat()
                .body("user.email", equalTo(newEmail.toLowerCase(Locale.ROOT)));
    }


    @Step("Проверка обновления name")
    public void checkNewName(ValidatableResponse response, String newName) {
        response
                .assertThat()
                .body("user.name", equalTo(newName));
    }

    @Step("Проверка обновления email, password, name для незарегестрированного пользователя")
    public void checkNotUpdateDataUser(ValidatableResponse badResponse) {
        badResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }


    @Step("Проверка ответа при создании пользователя без email, password или name")
    public void userResponseNegative(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @Step("Проверка ответа при авторизации пользователя с некорректным email или password")
    public void loggedNotSuccessfully(ValidatableResponse badLoginResponse) {
        badLoginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка ответа успешной авторизации пользователя для получения refreshToken")
    public String refreshLoggedInSuccessfully(ValidatableResponse loginResponse) {
        return loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("refreshToken");
    }

    @Step("Проверка ответа при успешном выходе пользователя из системы")
    public void logoutInSuccessfully(ValidatableResponse logoutResponse) {
        logoutResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("message", equalTo("Successful logout"));

    }
}
