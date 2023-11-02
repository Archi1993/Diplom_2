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

import java.util.List;


public class GetOrderTest {

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
    @DisplayName("Получение списка заказов зарегестрированого пользователя")
    @Description("Проверка успешного получения заказа авторизованного пользователя")
    public void getOrderPositiveTest() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        var order = OrderData.ORDER_WITH_ALL_CORRECT_INGREDIENTS;
        ValidatableResponse orderResponse = clientOrder.createOrderAuthorizedUser(accessToken, order);
        number = checkOrder.checkCreatedOrderSuccessfully(orderResponse);

        ValidatableResponse ordersResponse = clientOrder.getAuthorizedUserOrders(accessToken);
        checkOrder.checkGetFamousUserOrders(ordersResponse);
    }

    @Test
    @DisplayName("Получение списка заказов без регистрации")
    @Description("Проверка получения ошибки с кодом 401 при получении заказа не авторизованного пользователя")
    public void getOrderWithoutUserTest() {
        ValidatableResponse ordersResponse = clientOrder.getUnknownUserOrders();
        checkOrder.checkGetUnknownUserOrders(ordersResponse);
        flag = false;
    }

    @After
    public void deleteUser() {
        if (flag) {
            clientUser.deleteUser(accessToken);
        }
    }
}