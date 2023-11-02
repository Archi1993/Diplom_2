package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class OrderAssertion {

    @Step("Проверка ответа успешного создания заказа")
    public int checkCreatedOrderSuccessfully(ValidatableResponse orderResponse) {
        int number =  orderResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("order.number");
        return number;
    }

    @Step("Проверка ответа создания заказа без пользователя")
    public void checkCreateOrderWithoutUser(ValidatableResponse orderResponse) {
        orderResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка ответа создания заказа авторизованного пользователя с неверным хешем ингредиентов")
    public void checkCreateOrderWithUnknownIngredients(ValidatableResponse orderResponse) {
        orderResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    @Step("Проверка ответа создания заказа авторизованного пользователя без ингредиентов")
    public void checkCreateOrderWithoutIngredients(ValidatableResponse orderResponse) {
        orderResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка ответа получения списка заказов неавторизованного пользователя")
    public void checkGetFamousUserOrders(ValidatableResponse ordersResponse) {
        ordersResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true));
    }

    @Step("Проверка ответа получения списка заказов неавторизованного пользователя")
    public void checkGetUnknownUserOrders(ValidatableResponse ordersResponse) {
        ordersResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }


}
