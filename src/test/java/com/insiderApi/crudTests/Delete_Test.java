package com.insiderApi.crudTests;

import com.insiderApi.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.internal.http.HttpResponseException;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpException;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Delete_Test extends TestBase {


    @DisplayName("Positive Delete Test /pet/{petId}")
    @Test
    public void delete_JsonPath() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 13233343)
                .when()
                .delete("/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        int code = jsonPath.getInt("code");
        String type = jsonPath.getString("type");
        String message = jsonPath.getString("message");

        System.out.println("code = " + code);
        System.out.println("type = " + type);
        System.out.println("message = " + message);

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(200, jsonPath.getInt("code"));
        assertEquals("unknown", jsonPath.getString("type"));
        assertEquals("13233343", jsonPath.getString("message"));

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": 13233343,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 111222333444,\n" +
                        "    \"name\": \"Lemon3\"\n" +
                        "  },\n" +
                        "  \"name\": \"doggiessss\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"string\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 111222333444555,\n" +
                        "      \"name\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"sold\"\n" +
                        "}")
                .when().post("")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(13233343),
                        "category.name", equalTo("Lemon3"),
                        "name", is(equalTo("doggiessss")),
                        "name", startsWithIgnoringCase("do"),
                        "status", is("sold"));

    }


    @DisplayName("Negative Delete Test /pet/{petId}")//there is no id parameter which is equal to 40 it can not be deleted
    @Test
    public void delete2() {

       Response response = given().accept(ContentType.JSON)
                .and()
                .pathParam("petId", 40)
                .when().delete("/{petId}");


        assertEquals(404, response.getStatusCode());
        assertEquals("application/json", response.contentType());

    }


}
