package ru.yandex.praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserMethods;
import user.requests.RegisterUserRq;
import user.requests.LoginUserRq;
import common.ErrorRs;
import user.responses.RegisterLoginUserRs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LoginUserTest {

    UserMethods userMethods;

    @Before
    public void init() {
        userMethods = new UserMethods();
    }

    @DisplayName("Успешная авторизация пользователя.")
    @Test
    public void testUserIsLogged() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        assertThat(userMethods.isRegistered(user),is(true));
        RegisterLoginUserRs response = userMethods.login(LoginUserRq.getUserCredentials(user))
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(RegisterLoginUserRs.class);
        assertThat(response.isSuccess(),is(true));
        assertThat(response.getUser().getEmail(),is(user.getEmail()));
        assertThat(response.getUser().getName(),is(user.getName()));
    }

    @DisplayName("Ошибка при попытке авторизоваться под неверными учетными данными.")
    @Test
    public void testUserIsNotLoggedWithIncorrectUserCredentials() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        ErrorRs response = userMethods.login(LoginUserRq.getUserCredentials(user))
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("email or password are incorrect"));
    }

    @After
    public void tearDown(){userMethods.deleteUser();}

}
