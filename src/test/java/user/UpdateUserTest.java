package user;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.requests.UpdateUserRq;
import user.requests.LoginUserRq;
import user.requests.RegisterUserRq;
import common.ErrorRs;
import user.responses.UserRs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UpdateUserTest {

    UserMethods userMethods;

    @Before
    public void init() {
        userMethods = new UserMethods();
    }

    @DisplayName("Обновление данных пользователя с авторизацией.")
    @Test
    public void testUserUpdatedWithAuthorization(){
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        UpdateUserRq data = UpdateUserRq.getRandomUserData();
        assertThat(userMethods.isRegistered(user),is(true));
        String token = userMethods.isLogged(LoginUserRq.getUserCredentials(user)).getAccessToken();
        UserRs response = userMethods.updateUserData(data,token)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(UserRs.class);
        assertThat(response.isSuccess(),is(true));
        assertThat(response.getUser().getEmail(),is(data.getEmail()));
        assertThat(response.getUser().getName(),is(data.getName()));
    }

    @DisplayName("Ошибка при попытке обновить данные пользователя без авторизациии.")
    @Test
    public void testUserNotUpdatedWithoutAuthorization(){
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        UpdateUserRq data = UpdateUserRq.getRandomUserData();
        assertThat(userMethods.isRegistered(user),is(true));
        ErrorRs response = userMethods.updateUserData(data)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("You should be authorised"));
    }

    @After
    public void tearDown(){userMethods.deleteUser();}

}
