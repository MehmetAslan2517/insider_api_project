package com.insiderApi.utilities;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

public class TestBase {
    @BeforeAll
    public static void init() {
        baseURI = ConfigurationReader.getProperty("InsiderBaseUrl");
        basePath = "/pet";
    }
}
