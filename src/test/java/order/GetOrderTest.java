package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.Credentials;
import user.UserAssertion;
import user.UserClient;
import user.UserGenerator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class GetOrderTest {

    private final UserClient clientUser = new UserClient();
    private final UserAssertion checkUser = new UserAssertion();
    private final OrderClient clientOrder = new OrderClient();
    private final OrderAssertion checkOrder = new OrderAssertion();
    protected String accessToken;
    private boolean flag;
    protected int number;


    @Before
    public void setFlagAfter() {
        flag = true;
    }

    @Test
    @DisplayName("Получение списка заказов зарегестрированого пользователя")
    @Description("Проверка успешного получения заказа авторизованного пользователя")
    public void getOrderPositiveTest() {
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

        ValidatableResponse ordersResponse = clientOrder.getFamousUserOrders(accessToken);
        checkOrder.checkGetFamousUserOrders(ordersResponse);
    }

    @Test
    @DisplayName("Получение списка заказов без регистрации")
    @Description("Проверка получения ошибки с кодом 401 при получении заказа не авторизованного пользователя")
    public void getOrderWithoutUserTest() {
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

        ValidatableResponse ordersResponse = clientOrder.getUnknownUserOrders("");
        checkOrder.checkGetUnknownUserOrders(ordersResponse);
    }



    @After
    public void deleteUser() {
        ValidatableResponse delete = clientUser.deleteUser(accessToken);
        checkUser.deletedSuccessfully(delete);
    }


}