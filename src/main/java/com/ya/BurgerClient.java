package com.ya;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class BurgerClient extends BurgerRestClient {

    public ValidatableResponse createOrder(Burger burger, User user) {
        if (user.getAccessToken() != null) {
            return given()
                    .spec(getBaseSpec())
                    .header("Authorization", user.getAccessToken())
                    .when()
                    .body(burger)
                    .post("orders")
                    .then();
        } else {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .body(burger)
                    .post("orders")
                    .then();
        }

    }

}