package com.insiderApi.crudTests;

import com.insiderApi.utilities.TestBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class Post_Test extends TestBase {

    @DisplayName("Positive POST /pet/{petId}/uploadImage")
    @Test
    public void postPetId_UploadImage() {

        RestAssured.given()
                .pathParam("petId", 485)
                .contentType(ContentType.MULTIPART)
                .multiPart("additionalMetadata", "cat image3")
                .multiPart("file", new File("C:\\Users\\bkryl\\OneDrive\\Masaüstü\\Sugar.jpg"))
                .when()
                .post("/{petId}/uploadImage")
                .then()
                .assertThat()
                .statusCode(200);
    }


    @DisplayName("Negative POST /pet/{petId}/uploadImage")
    @Test // it can not be uploaded image because image's path way is not written. Test failed
    public void postPetId_UploadImage2() {

        RestAssured.given()
                .pathParam("petId", 485)
                .contentType(ContentType.MULTIPART)
                .multiPart("additionalMetadata", "cat image3")
                .multiPart("file", new File(""))
                .when()
                .post("/{petId}/uploadImage");
    }



    @DisplayName("Positive POST /pet")
    @Test
    public void postRequest_WithHamcrest() {
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


    @DisplayName("Negative POST /pet") // wrong content type. TEXT is not written swagger document (POST)
    @Test
    public void postRequest2() {
        given().contentType(ContentType.TEXT)
                .body("{\n" +
                        "  \"id\": 987654321,\n" +
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
                        "  \"status\": \"new\"\n" +
                        "}")
                .when().post("")
                .then().statusCode(415);
    }

    @DisplayName("Positive POST /pet/{petId}")
    @Test //This is a bug, According to the Swagger. Status code should be 200, but we get 404
    //
    public void postRequest3() {

        RestAssured.given()
                .pathParam("petId", 485)
                .contentType(ContentType.MULTIPART)
                .multiPart("name", "catiee")
                .multiPart("status","sold" )
                .when()
                .post("/{petId}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", is("catiee"))
                .body("status", is("sold"));


    }

}
