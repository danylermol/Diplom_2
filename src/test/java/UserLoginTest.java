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

public class UserLoginTest {

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
    @DisplayName("Логин под существующим пользователем")
    public void loginWithValidCredentials(){
        ValidatableResponse validatableResponse = userClient.login(user);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("success",equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверной почтой")
    public void loginWithIncorrectEmail(){
        user.setEmail("test");
        ValidatableResponse validatableResponse = userClient.login(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message",equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithIncorrectPassword(){
        user.setPassword("test");
        ValidatableResponse validatableResponse = userClient.login(user);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message",equalTo("email or password are incorrect"));
    }

}