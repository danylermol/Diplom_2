import com.ya.User;
import com.ya.UserClient;
import com.ya.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserEditProfileTest {

    UserClient userClient;
    User user, newUser;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        newUser = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("Изменение почты авторизованного пользователя")
    public void updateUserEmailForAuthorizedUser(){
        ValidatableResponse validatableResponse = userClient.edit(user,newUser);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("user.email",equalTo(newUser.getEmail()));
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void updateUserNameForAuthorizedUser(){
        ValidatableResponse validatableResponse = userClient.edit(user,newUser);
        validatableResponse.assertThat().statusCode(SC_OK);
        validatableResponse.assertThat().body("user.name",equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя")
    public void updateCredentialsForUnauthorizedUser(){
        user.setAccessToken(null);
        ValidatableResponse validatableResponse = userClient.edit(user,newUser);
        validatableResponse.assertThat().statusCode(SC_UNAUTHORIZED);
        validatableResponse.assertThat().body("message",equalTo("You should be authorised"));
    }
}