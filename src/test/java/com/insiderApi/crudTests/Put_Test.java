package com.insiderApi.crudTests;

import com.insiderApi.pojo.Pet;
import com.insiderApi.utilities.TestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class Put_Test extends TestBase {


    @DisplayName("Positive PUT to /pet") // status updated as "available" instead of "sold"
    @Test
    public void putRequest_WithHamcrest() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": 12,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"dogs\"\n" +
                        "  },\n" +
                        "  \"name\": \"Fido\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"string\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available\"\n" +
                        "}")
                .when().put("")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(12),
                        "category.name", equalTo("dogs"),
                        "name", is(equalTo("Fido")),
                        "name", startsWithIgnoringCase("Fi"),
                        "status", is("available")); // status updated before sold now available
    }

    @DisplayName("Negative PUT to /pet") // "TEXT" is not written swagger document (PUT). So that reason it is unsupported media type
    @Test
    public void putRequest_WithHamcrest2() {
        given().contentType(ContentType.TEXT)
                .body("{\n" +
                        "  \"id\": 987654321,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 111222333444,\n" +
                        "    \"name\": \"Lemon18\"\n" +
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
                        "  \"status\": \"new\"\n" +
                        "}")
                .when().put("")
                .then().statusCode(415);
    }

    @DisplayName("Positive PUT to /pet") // status updated as "available" instead of "sold"
    @Test
    public void putRequest_WithHamcrest_Pojo() {
        Pet pet = new Pet();
        pet.setId(12);
        Map<String, Object> ctgry = new HashMap<>();
        ctgry.put("id",0);
        ctgry.put("name","dogs");
        pet.setCategory(ctgry);
        pet.setName("Fido");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("string");
        pet.setPhotoUrls(photoUrls);
        Map<String, Object> tag =new HashMap<>();
        tag.put("id",0);
        tag.put("name","string");
        List<Map<String,Object>> tags = new ArrayList<>();
        tags.add(tag);
        pet.setTags(tags);
        pet.setStatus("available");

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pet)
                .when().put("")
                .then().statusCode(200).contentType("application/json")
                .body("id", is(12),
                        "category.name", equalTo("dogs"),
                        "name", is(equalTo("Fido")),
                        "name", startsWithIgnoringCase("Fi"),
                        "status", is("available")); // status updated before sold now available
    }

    @DisplayName("Negative PUT to /pet") // status updated as "available" instead of "sold" with invalid ID
    @Test //This is a bug.According to the Swagger It should be 400 invalid id supplied.
    // (Because ıd accept int64, but we pass more than that) But We get 200.
    public void putRequest_WithHamcrest_Pojo2() {
        Pet pet = new Pet();
        pet.setId(12345678901234567L);
        Map<String, Object> ctgry = new HashMap<>();
        ctgry.put("id",0);
        ctgry.put("name","dogs");
        pet.setCategory(ctgry);
        pet.setName("Fido");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("string");
        pet.setPhotoUrls(photoUrls);
        Map<String, Object> tag =new HashMap<>();
        tag.put("id",0);
        tag.put("name","string");
        List<Map<String,Object>> tags = new ArrayList<>();
        tags.add(tag);
        pet.setTags(tags);
        pet.setStatus("available");

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pet)
                .when().put("")
                .then().statusCode(400).contentType("application/json");

    }

}
