package ru.yandex.praktikum;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;

public class LoginCourierTest extends BaseTest {
    private String login;
    private String password;
    private String name;
    private String userId;

    @Before
    public void setUp() {
        Random rand = new Random();

        RestAssured.baseURI = baseURL;
        login = "login" + rand.nextInt();
        password = "pass" + rand.nextInt();
        name = "name" + rand.nextInt();

        MakeCourierRequest requestData = new MakeCourierRequest(login, password, name);
        given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(requestData).when().post("/api/v1/courier");
    }

    @After
    public void teardown() {
        LoginRequest loginRequest = new LoginRequest(login, password);

        LoginResponse logResponse = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(loginRequest).when().post("/api/v1/courier/login").as(LoginResponse.class);
        userId = logResponse.getId();

        DeleteCourierRequest deleteRequest = new DeleteCourierRequest(userId);

        given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(deleteRequest).when().delete("/api/v1/courier/"+userId);
    }

    @Test
    @DisplayName("Успешный вход в учетную запись курьера")
    public void successLogin() {
        LoginRequest loginRequest = new LoginRequest(login, password);
        Response response = makeLoginRequest(loginRequest);
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("После того как мы залогинились, нам вернулся идентификатор курьера")
    public void successLoginReturnId() {
        LoginRequest loginRequest = new LoginRequest(login, password);

        LoginResponse logResponse = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(loginRequest).when().post("/api/v1/courier/login").as(LoginResponse.class);
        userId = logResponse.getId();
        assertNotEquals(userId, null);
    }

    private String removeParameterFromLoginJSON(String paramName) {
        LoginRequest loginRequest = new LoginRequest(login, password);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(loginRequest);
        jsonElement.getAsJsonObject().remove(paramName);

        return gson.toJson(jsonElement);
    }

    private Response makeLoginRequest(String json) {
        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(json).when().post("/api/v1/courier/login");
        return response;
    }

    private Response makeLoginRequest(LoginRequest loginRequest) {
        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(loginRequest).when().post("/api/v1/courier/login");
        return response;
    }

    private void checkStatusCodeAndErrorText(Response response, int code, String text) {
        response.then().statusCode(code);
        response.then().body("message", equalTo(text));
    }

    @Test
    @DisplayName("При попытке залогиниться не указав логин получаем ошибку 400")
    public void loginWithoutLoginParameterFailed() {
        String json = removeParameterFromLoginJSON("login");
        Response response = makeLoginRequest(json);
        checkStatusCodeAndErrorText(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("При попытке залогиниться не указав пароль получаем ошибку 400")
    public void loginWithoutPasswordParameterFailed() {
        String json = removeParameterFromLoginJSON("password");
        Response response = makeLoginRequest(json);
        checkStatusCodeAndErrorText(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Попытка залогиниться по несуществующей учетной записи")
    public void userIsNotExists() {
        Random rand = new Random();
        LoginRequest loginRequest = new LoginRequest("n"+rand.nextInt(),
                                                  "p"+rand.nextInt());
        Response response = makeLoginRequest(loginRequest);
        checkStatusCodeAndErrorText(response, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Аутентификация с неправильным логином")
    public void loginWithWrongLogin() {
        Random rand = new Random();
        LoginRequest loginRequest = new LoginRequest("n"+rand.nextInt(), password);
        Response response = makeLoginRequest(loginRequest);
        checkStatusCodeAndErrorText(response, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Аутентификация с неправильным паролем")
    public void loginWithWrongPassword() {
        Random rand = new Random();
        LoginRequest loginRequest = new LoginRequest(login, "p"+rand.nextInt());
        Response response = makeLoginRequest(loginRequest);
        checkStatusCodeAndErrorText(response, 404, "Учетная запись не найдена");
    }
}
