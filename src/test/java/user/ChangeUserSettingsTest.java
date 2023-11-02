package user;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class ChangeUserSettingsTest {
    private final UserClient clientUser = new UserClient();
    private final UserAssertion checkUser = new UserAssertion();
    protected String accessToken;
    protected String refreshToken;


    @Test
    @DisplayName("Изменение email для зарегестрированого пользователя")
    @Description("Проверка получения успешного ответа при изменении email зарегистрированного пользователя")
    public void successfullyUpdateEmailUser() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse); // можно конечно было добавить сразу данные вместо переменной в скобках loginResponse и сделать для похожих случаев также, но для меня, как для начинающего автоматизатора, так легче чтобы не запутаться в коде.

        String newEmail = RandomStringUtils.randomAlphabetic(10) + "@newexample.com";
        user.setEmail(newEmail);

        clientUser.updateUser(accessToken, user);
        ValidatableResponse updatedUser = clientUser.getUserInfo(accessToken);
        checkUser.checkNewEmail(updatedUser, newEmail);
    }

    @Test
    @DisplayName("Изменение password для зарегестрированого пользователя")
    @Description("Проверка получения успешного ответа при изменении password зарегистрированного пользователя")
    public void successfullyUpdatePasswrodUser() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        String newPassword = "TestPassword" + RandomStringUtils.randomAlphabetic(3);
        user.setPassword(newPassword);

        clientUser.updateUser(accessToken, user);
        refreshToken = checkUser.refreshLoggedInSuccessfully(loginResponse);
        clientUser.logoutUser(refreshToken);

        var newCreds = Credentials.from(user);
        ValidatableResponse newLoginResponse = clientUser.loginUser(newCreds);
        accessToken = checkUser.loggedInSuccessfully(newLoginResponse);
    }

    @Test
    @DisplayName("Изменение name для зарегестрированого пользователя")
    @Description("Проверка получения успешного ответа при изменении name зарегистрированного пользователя")
    public void successfullyUpdateNameUser() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        String newName = "TestName" + RandomStringUtils.randomAlphabetic(5);
        user.setName(newName);

        clientUser.updateUser(accessToken, user);
        ValidatableResponse updatedUser = clientUser.getUserInfo(accessToken);
        checkUser.checkNewName(updatedUser, newName);
    }

    @Test
    @DisplayName("Изменение email для незарегестрированого пользователя")
    @Description("Проверка получения ответа при изменении email для незарегистрированного пользователя")
    public void notSuccessfullyUpdateEmailUser() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user); //Требуется корректная авторизация пользователя, для его успешного удаления после теста
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        String newEmail = RandomStringUtils.randomAlphabetic(10) + "@newexample.com";
        user.setEmail(newEmail);

        ValidatableResponse badResponse = clientUser.updateUser("", user);
        checkUser.checkNotUpdateDataUser(badResponse);
    }

    @Test
    @DisplayName("Изменение password для незарегестрированого пользователя")
    @Description("Проверка получения ответа при изменении password для незарегистрированного пользователя")
    public void notSuccessfullyUpdatePasswordUser() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user); //Требуется корректная авторизация пользователя, для его успешного удаления после теста
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        String newPassword = "TestPassword" + RandomStringUtils.randomAlphabetic(3);
        user.setPassword(newPassword);

        ValidatableResponse badResponse = clientUser.updateUser("", user);
        checkUser.checkNotUpdateDataUser(badResponse);
    }

    @Test
    @DisplayName("Изменение name для незарегестрированого пользователя")
    @Description("Проверка получения ответа при изменении name для незарегистрированного пользователя")
    public void notSuccessfullyUpdateNameUser() {
        var user = UserGenerator.random();
        clientUser.createUser(user);

        var creds = Credentials.from(user); //Требуется корректная авторизация пользователя, для его успешного удаления после теста
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);

        String newName = "TestName" + RandomStringUtils.randomAlphabetic(5);
        user.setName(newName);

        ValidatableResponse badResponse = clientUser.updateUser("", user);
        checkUser.checkNotUpdateDataUser(badResponse);
    }

    @After
    public void deleteUser() {
        clientUser.deleteUser(accessToken);
    }
}