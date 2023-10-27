package user;

public class Credentials {
    private String email;
    private String password;



    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Credentials() {
    }

    public static Credentials from(User user) {
        return new Credentials(user.getEmail(), user.getPassword());
    }
    public static Credentials fromWithoutEmail(User user) {
        return new Credentials("", user.getPassword());
    }

    public static Credentials fromWithoutPassword(User user) {
        return new Credentials(user.getEmail(), "");
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
