package com.ya;

import com.github.javafaker.Faker;

public class TestDataGenerator {
    public static User getRandomUser() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
    }

    public static String[] getIngredients() {
        String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"};
        return ingredients;

    }

}