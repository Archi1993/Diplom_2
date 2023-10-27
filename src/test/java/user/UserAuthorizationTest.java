package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserAuthorizationTest {
    private final UserClient clientUser = new UserClient();
    private final UserAssertion checkUser = new UserAssertion();
    protected String accessToken;



    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("Проверка полученного ответа при успешной авторизации пользователя")
    public void authorizationUser() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    @Description("Проверка полученного ответа с кодом 401 при авторизации пользователя без логина")
    public void authorizationUserWithoutEmail() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.fromWithoutEmail(user);
        ValidatableResponse badLoginResponse = clientUser.loginUser(creds);
        checkUser.loggedNotSuccessfully(badLoginResponse);

        var incompleteСreds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(incompleteСreds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    @Description("Проверка полученного ответа с кодом 401 при авторизации пользователя без пароля")
    public void authorizationUserWithoutPassword() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.fromWithoutPassword(user);
        ValidatableResponse badLoginResponse = clientUser.loginUser(creds);
        checkUser.loggedNotSuccessfully(badLoginResponse);

        var incompleteСreds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(incompleteСreds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));
    }


    @After
    public void deleteUser() {
            ValidatableResponse delete = clientUser.deleteUser(accessToken);
            checkUser.deletedSuccessfully(delete);
    }

}