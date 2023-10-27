package order;

import org.junit.After;
import user.Credentials;
import user.UserAssertion;
import user.UserClient;
import user.UserGenerator;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class CreateOrderTest {
    private final UserClient clientUser = new UserClient();
    private final UserAssertion checkUser = new UserAssertion();
    private final OrderClient clientOrder = new OrderClient();
    private final OrderAssertion checkOrder = new OrderAssertion();
    protected String accessToken;
    protected int number;


    @Test
    @DisplayName("Создание заказа авторизованного пользователя")
    @Description("Проверка полученного ответа при создании заказа авторизованного пользователя")
    public void createNewSuccessfullyOrder() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));

        ValidatableResponse orderResponse = clientOrder.createOrderAuthorizedUser(accessToken);
        number = checkOrder.checkCreatedOrderSuccessfully(orderResponse);
        assert number != 0;
    }

    @Test
    @DisplayName("Создание заказа неавторизованного пользователя")
    @Description("Проверка полученного ответа при создании заказа неавторизованного пользователя")
    public void createNewUnSuccessfullyOrder() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));

        ValidatableResponse orderResponse = clientOrder.createOrderUnAuthorizedUser(""); // должен придти код ошибки 401, а приходит 200 - баг
        checkOrder.checkCreateOrderWithoutUser(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя с неверными хешами ингредиентов")
    @Description("Проверка полученного ответа при создании заказа авторизованного пользователя с неверными хешами ингредиентов")
    public void createOrderWithIncorrectIngredient() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));

        ValidatableResponse orderResponse = clientOrder.createOrderWithUnknownIngredients(accessToken);
        checkOrder.checkCreateOrderWithUnknownIngredients(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя без ингредиентов")
    @Description("Проверка полученного ответа при создании заказа авторизованного пользователя без ингредиентов")
    public void createOrderWithoutIngredient() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));

        ValidatableResponse orderResponse = clientOrder.createWithoutIngredients(accessToken);
        checkOrder.checkCreateOrderWithoutIngredients(orderResponse);
    }

    @After
    public void deleteUser() {
        ValidatableResponse delete = clientUser.deleteUser(accessToken);
        checkUser.deletedSuccessfully(delete);
    }
}