package user;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateUserTest {


    private final UserClient clientUser = new UserClient();
    private final UserAssertion checkUser = new UserAssertion();
    protected String accessToken;
    private boolean flag;


    @Before
    public void setFlagAfter() {
        flag = true;
    }


    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Проверка успешного создания пользователя c рандомными данными, затем авторизацией на сайт и получением accessToken пользователя и последующем удалении пользователя по accessToken")
    public void createNewUser() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user); // не получается удалить пользователя без его авторизации, поэтому добавил авторизацию чтобы потом почистить созданного пользователя
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));
    }


    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("(Проверка получения ответа с кодом 403 при попытке создания существующего пользователя в системе")
    public void creatureUserIsAlreadyRegisteredTest() {
        var user = UserGenerator.random();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.createdSuccessfully(response);

        var creds = Credentials.from(user); // не получается удалить пользователя без его авторизации, поэтому добавил авторизацию чтобы потом почистить созданного пользователя
        ValidatableResponse loginResponse = clientUser.loginUser(creds);
        accessToken = checkUser.loggedInSuccessfully(loginResponse);
        assertThat("Failed to login!", accessToken, is(notNullValue()));

        ValidatableResponse responseConflict = clientUser.createUser(user);
        checkUser.createAnExistingUser(responseConflict);
    }


    @Test
    @DisplayName("Cоздание пользователя без email")
    @Description("Проверка получения ответа с кодом 403 при создании пользователя без email")
    public void creatureUserWithoutEmailTest() {
        var user = UserGenerator.genericWithoutEmail();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.userResponseNegative(response);
        flag = false;
    }

    @Test
    @DisplayName("Cоздание пользователя без пароля")
    @Description("Проверка получения ответа с кодом 403 при создании пользователя без пароля")
    public void creatureUserWithoutPasswordTest() {
        var user = UserGenerator.genericWithoutPassword();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.userResponseNegative(response);
        flag = false;
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка получения ответа с кодом 403 при создании пользователя без имени")
    public void creatureUserWithoutNameTest() {
        var user = UserGenerator.genericWithoutName();
        ValidatableResponse response = clientUser.createUser(user);
        checkUser.userResponseNegative(response);
        flag = false;
    }


    @After
    public void deleteUser() {
        if (flag) {
            ValidatableResponse delete = clientUser.deleteUser(accessToken);
            checkUser.deletedSuccessfully(delete);
        }
    }


}