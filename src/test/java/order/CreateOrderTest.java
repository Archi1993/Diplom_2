package order;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.junit.After;
import org.junit.Before;
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
    private boolean flag;

    @Before
    public void setFlagAfter() {
        flag = true;
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя")
    @Description("Проверка полученного ответа при создании заказа авторизованного пользователя")
    public void createNewSuccessfullyOrder() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        ValidatableResponse orderResponse = clientOrder.createOrderAuthorizedUser(accessToken);
        number = checkOrder.checkCreatedOrderSuccessfully(orderResponse);
        assertThat("Order not created!", number, is(notNullValue()));
    }

    @Test
    @DisplayName("Создание заказа неавторизованного пользователя")
    @Description("Проверка полученного ответа при создании заказа неавторизованного пользователя")
    public void createNewUnSuccessfullyOrder() {
        flag = false;
        ValidatableResponse orderResponse = clientOrder.createOrderUnAuthorizedUser(); // должен придти код ошибки 401, а приходит 200 - баг
        checkOrder.checkCreateOrderWithoutUser(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя с неверными хешами ингредиентов")
    @Description("Проверка полученного ответа при создании заказа авторизованного пользователя с неверными хешами ингредиентов")
    public void createOrderWithIncorrectIngredient() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        ValidatableResponse orderResponse = clientOrder.createOrderWithUnknownIngredients(accessToken);
        checkOrder.checkCreateOrderWithUnknownIngredients(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя без ингредиентов")
    @Description("Проверка полученного ответа при создании заказа авторизованного пользователя без ингредиентов")
    public void createOrderWithoutIngredient() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        ValidatableResponse orderResponse = clientOrder.createWithoutIngredients(accessToken);
        checkOrder.checkCreateOrderWithoutIngredients(orderResponse);
    }

    @After
    public void deleteUser() {
        if (flag) {
            clientUser.deleteUser(accessToken);
        }
    }
}