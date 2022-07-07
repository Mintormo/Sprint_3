package ru.yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class GetOrderListTest extends BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = baseURL;
    }

    @Test
    @DisplayName("При получении списка заказов, убеждаемся, что он не пустой")
    public void orderListIsNotEmpty() {
        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .get("/api/v1/orders");
        response.then().statusCode(200);
        GetOrderListResponse orderResponse = response.as(GetOrderListResponse.class);
        assertTrue(orderResponse.orders.size() > 0);
    }
}
