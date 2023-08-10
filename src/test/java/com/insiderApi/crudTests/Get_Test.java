package com.insiderApi.crudTests;

import com.insiderApi.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Get_Test extends TestBase {

    @DisplayName("Positive testing GET request to /pet/findByStatus")
    @ParameterizedTest
    @ValueSource(strings = {"available","pending","sold"})
    public void getMethod_queryParam(String s) {
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("status", s)
                .log().all()
                .when()
                .get("/findByStatus");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.header("Content-Type"));
        List<String> status = response.path(s);
        for (String stat : status) {
            assertEquals(s,stat);
        }
        response.prettyPrint();
    }

    @DisplayName("Negative testing GET request to /pet/findByStatus")
    @Test //as a value "ready" is not written in swagger document for queryParam. You can not get any info in response the body,
    public void getMethod_queryParam2() {
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("status", "ready")
                .log().all()
                .when()
                .get("/findByStatus");


        assertEquals("application/json", response.header("Content-Type"));
        assertEquals(400, response.statusCode());//This is a bug.
        // According to the swagger, available values for status are available, pending and sold.
        // ready is not one of them, so we should get 400 status code, but we get 200.
        // because of that our test failed
        response.prettyPrint();
    }

    @DisplayName("Positive testing GET request to /pet/{petID}")
    @Test
    public void getMethod_pathParam() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 1)
                .when()
                .get("/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(1, jsonPath.getInt("id"));

    }


    @DisplayName("Negative testing GET request to /pet/{petID} pet not found")
    @Test // there is no pet wit petID 250000000
    public void getMethod_pathParam2() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 250000000)
                .when()
                .get("/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(404, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals("error", jsonPath.getString("type"));

    }

    @DisplayName("Negative testing GET request to /pet/{petID} invalid id")
    @Test // there is a bug according to the swagger,
    // we should get 400 for invalid id (in this test we use bigger than int64)
    // we get 404
    public void getMethod_pathParam3() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 1234567890123456L)
                .when()
                .get("/{petId}");

        JsonPath jsonPath = response.jsonPath();
        assertEquals(400, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals("error", jsonPath.getString("type"));

    }

}
