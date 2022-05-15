import com.ya.User;
import com.ya.UserClient;
import com.ya.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserOrdersTest {

    UserClient userClient;
    User user;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }


    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersForUnauthorizedUser(){
        user.setAccessToken(null);
        ValidatableResponse validatableResponse = userClient.getOrders(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message",equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersForAuthorizedUser(){
        ValidatableResponse validatableResponse = userClient.getOrders(user);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success",equalTo(true));
    }
}