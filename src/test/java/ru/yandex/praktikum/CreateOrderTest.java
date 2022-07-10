package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {
    private List<String> colors;
    private long track;

    @Before
    public void setUp() {
        RestAssured.baseURI = baseURL;
    }

    @After
    public void teardown() {
        CancelOrder cancelOrder = new CancelOrder(track);

        given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(cancelOrder).when().put("/api/v1/orders/cancel");
    }

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Color - {0}")
    public static Object[][] getData() {
        return new Object[][] {
                {List.of("GRAY")},
                {List.of("BLACK")},
                {List.of("BLACK", "GRAY")},
                {List.of()}
        };
    }

    @Test
    @DisplayName("Успешное создание нового заказа с различными цветами товара")
    public void createOrder() {
        Random rand = new Random();
        CreateOrderRequest createOrder = new CreateOrderRequest(
                "fn" + rand.nextInt(),
                "ln" + rand.nextInt(),
                "adr" + rand.nextInt(),
                "mt" + rand.nextInt(),
                "ph" + rand.nextInt(),
                rand.nextInt(100),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                "comm" + rand.nextInt(),
                colors.toArray(new String[]{})
        );

        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(createOrder).when().post("/api/v1/orders");

        response.then().statusCode(201);
        CreateOrderResponse answer = response.as(CreateOrderResponse.class);
        track = answer.getTrack();
        assertNotNull(track);
    }

}
