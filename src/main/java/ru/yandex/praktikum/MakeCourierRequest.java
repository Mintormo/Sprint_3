package ru.yandex.praktikum;

public class MakeCourierRequest {
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String password;
    private String firstName;

    public MakeCourierRequest(String login, String password, String firstName) {
        setLogin(login);
        setPassword(password);
        setFirstName(firstName);
    }

    public MakeCourierRequest() {

    }
}
