import com.ya.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {

    User user;
    UserClient userClient;
    Burger burger;
    BurgerClient burgerClient;

    @Before
    public void setUp() {
        user = TestDataGenerator.getRandomUser();
        userClient = new UserClient();
        burger = new Burger();
        burgerClient = new BurgerClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrderForAuthorizedUser() {
        burger.setIngredients(TestDataGenerator.getIngredients());
        ValidatableResponse validatableResponse = burgerClient.createOrder(burger, user);


        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void createOrderForUnauthorizedUser() {
        user.setAccessToken(null);
        burger.setIngredients(TestDataGenerator.getIngredients());
        ValidatableResponse validatableResponse = burgerClient.createOrder(burger, user);


        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void createOrderWithIngredients() {
        burger.setIngredients(TestDataGenerator.getIngredients());

        ValidatableResponse validatableResponse = burgerClient.createOrder(burger, user);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пустого заказа")
    public void createEmptyOrder() {
        ValidatableResponse validatableResponse = burgerClient.createOrder(burger, user);
        validatableResponse.assertThat().statusCode(SC_BAD_REQUEST);
        validatableResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidHashes() {
        String[] incorrectIngredients = {"test", "test"};
        burger.setIngredients(incorrectIngredients);
        ValidatableResponse validatableResponse = burgerClient.createOrder(burger, user);
        validatableResponse.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}