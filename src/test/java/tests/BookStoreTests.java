package tests;

import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {

    @BeforeAll
    @Tag("bookstoretest")
    static void setup() {
        RestAssured.baseURI = "https://demoqa.com";
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("")
    @Test
    @Tag("bookstoretest")
    void noLogsTest() {
        given()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("")
    @Test
    @Tag("bookstoretest")
    void withAllLogsTest() {
        given()
                .log().all()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)));
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("")
    @Test
    @Tag("bookstoretest")
    void authorizeTest() {
        String data = "{" +
                "  \"userName\": \"alex\"," +
                "  \"password\": \"asdsad#frew_DFS2\"" +
                "}";

//        Map<String, String> data = new HashMap<>();
//        data.put("userName", "alex");
//        data.put("password", "asdsad#frew_DFS2");

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(data.toString())
                .when()
                .log().uri()
                .log().body()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("")
    @Test
    @Tag("bookstoretest")
    void authorizeWithListenerTest() {
        String data = "{" +
                "  \"userName\": \"alex\"," +
                "  \"password\": \"asdsad#frew_DFS2\"" +
                "}";

        given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .accept("application/json")
                .body(data.toString())
                .when()
                .log().uri()
                .log().body()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("")
    @Test
    void authorizeWithTemplatesTest() {
        String data = "{" +
                "  \"userName\": \"alex\"," +
                "  \"password\": \"asdsad#frew_DFS2\"" +
                "}";
        step("Generate token", () ->
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType("application/json")
                        .accept("application/json")
                        .body(data.toString())
                        .when()
                        .log().uri()
                        .log().body()
                        .post("https://demoqa.com/Account/v1/GenerateToken")
                        .then()
                        .log().body()
                        .body("status", is("Success"))
                        .body("result", is("User authorized successfully."))
        );
        step("Any UI action");
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("")
    @Test
    void authorizeWithSchemeTest() {
        String data = "{" +
                "  \"userName\": \"alex\"," +
                "  \"password\": \"asdsad#frew_DFS2\"" +
                "}";

        given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .accept("application/json")
                .body(data.toString())
                .when()
                .log().uri()
                .log().body()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body(matchesJsonSchemaInClasspath("schemas/GenerateTokenScheme.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }
}
