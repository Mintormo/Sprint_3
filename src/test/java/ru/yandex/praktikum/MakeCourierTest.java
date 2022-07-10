package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.Matchers.*;
import java.util.Random;
import static io.restassured.RestAssured.*;

public class MakeCourierTest extends BaseTest {
    private String login;
    private String password;
    private String name;

    @Before
    public void setUp() {
        Random rand = new Random();

        RestAssured.baseURI = baseURL;
        login = "login" + rand.nextInt();
        password = "pass" + rand.nextInt();
        name = "name" + rand.nextInt();
    }

    @After
    public void teardown() {
        LoginRequest loginRequest = new LoginRequest(login, password);

        LoginResponse loginResponse = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(loginRequest).when().post("/api/v1/courier/login").as(LoginResponse.class);

        String userId = loginResponse.getId();

        DeleteCourierRequest deleteRequest = new DeleteCourierRequest(userId);

        Response responseDeleteCourier = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(deleteRequest).when().delete("/api/v1/courier/"+userId);
        responseDeleteCourier.then().statusCode(200);
        responseDeleteCourier.then().assertThat().body("ok", is(true));
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void makingCourierIsSuccess() {
        MakeCourierRequest requestData = new MakeCourierRequest(login, password, name);

        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                    .and().body(requestData).when().post("/api/v1/courier");

        response.then().statusCode(201);
        response.then().assertThat().body("ok", is(true));
    }

    private Response makeCourier(String login, String password, String name) {
        MakeCourierRequest requestData = new MakeCourierRequest(login, password, name);

        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(requestData).when().post("/api/v1/courier");
        return response;
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void makeIdenticalCouriersIsFailed() {
        Response response = makeCourier(login, password, name);
        response.then().statusCode(201);
        response.then().assertThat().body("ok", is(true));

        response = makeCourier(login, password, name);
        response.then().statusCode(409);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Нельзя создать двух курьеров с разными именами и одинаковым логином")
    public void makeCouriersWithSameLoginAndDifferentNames() {
        Random rand = new Random();
        Response response = makeCourier(login, password, name);
        response.then().statusCode(201);
        response.then().assertThat().body("ok", is(true));

        response = makeCourier(login, password + rand.nextInt(100), name + rand.nextInt(100));
        response.then().statusCode(409);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }
}
