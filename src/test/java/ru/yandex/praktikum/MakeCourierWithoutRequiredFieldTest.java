package ru.yandex.praktikum;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Random;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class MakeCourierWithoutRequiredFieldTest extends BaseTest {
    private String login;
    private String password;
    private String name;
    private final String removedFieldInMakeCourierBody;
    private final int expectedCode;
    private final String expectedMessage;

    public MakeCourierWithoutRequiredFieldTest(String removedFieldInMakeCourierBody,
                                               int expectedCode,
                                               String exptectedMessage) {
        this.removedFieldInMakeCourierBody = removedFieldInMakeCourierBody;
        this.expectedCode = expectedCode;
        this.expectedMessage = exptectedMessage;
    }

    @Parameterized.Parameters(name = "Removed field - {0}, expected status - {1}")
    public static Object[][] getData() {
        return new Object[][] {
                {"login", 400, "Недостаточно данных для создания учетной записи"},
                {"password", 400, "Недостаточно данных для создания учетной записи"},
                {"firstName", 400, "Недостаточно данных для создания учетной записи"}
        };
    }

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

        given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(deleteRequest).when().delete("/api/v1/courier/" + userId);
    }

    @Test
    @DisplayName("Попытка создать курьера не указав одно из полей запроса")
    public void makeCourierWithoutRequiredFieldInBody() {
        MakeCourierRequest requestData = new MakeCourierRequest(login, password, name);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(requestData);
        jsonElement.getAsJsonObject().remove(removedFieldInMakeCourierBody);

        String json = gson.toJson(jsonElement);

        Response response = given().header("Content-type", "application/json")
                .relaxedHTTPSValidation().auth().oauth2(token)
                .and().body(json).when().post("/api/v1/courier");

        response.then().statusCode(expectedCode);
        response.then().assertThat().body("message", equalTo(expectedMessage));
    }
}
