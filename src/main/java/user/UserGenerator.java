package user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    protected static String email = "AutoTest-Archi@yandex.ru";
    protected static String password = "password123";
    protected static String name = "Archi";


    public static User random() {
        return new User("AutoTest" + RandomStringUtils.randomAlphanumeric(5, 10) + "@example.ru", password, name);
    }

    public static User genericWithoutEmail() {
        return new User(null, password, name);
    }

    public static User genericWithoutPassword() {
        return new User(email, null, name);
    }
    public static User genericWithoutName() {
        return new User(email, password, null);
    }

}
