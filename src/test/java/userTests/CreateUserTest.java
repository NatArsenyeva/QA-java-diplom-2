package userTests;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import common.ErrorRs;
import user.requests.RegisterUserRq;
import user.UserMethods;
import user.responses.RegisterLoginUserRs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {

    UserMethods userMethods;

    @Before
    public void init() {
        userMethods = new UserMethods();
    }

    @Test
    public void testRegisteredNewUser() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        RegisterLoginUserRs response = userMethods.register(user)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(RegisterLoginUserRs.class);
        assertThat(response.isSuccess(),is(true));
        assertThat(response.getUser().getEmail(),is(user.getEmail()));
        assertThat(response.getUser().getName(),is(user.getName()));
    }

    @Test
    public void testNotRegisteredTheSameUser() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        assertThat(userMethods.isRegistered(user),is(true));
        ErrorRs response = userMethods.register(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("User already exists"));
    }

    @Test
    public void testNotRegisteredWithoutEmail() {
        RegisterUserRq user = RegisterUserRq.getRandomUserWithoutEmail();
        ErrorRs response = userMethods.register(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("Email, password and name are required fields"));
    }

    @Test
    public void testNotRegisteredWithoutPassword() {
        RegisterUserRq user = RegisterUserRq.getRandomUserWithoutPassword();
        ErrorRs response = userMethods.register(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("Email, password and name are required fields"));
    }

    @Test
    public void testNotRegisteredWithoutName() {
        RegisterUserRq user = RegisterUserRq.getRandomUserWithoutName();
        ErrorRs response = userMethods.register(user)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("Email, password and name are required fields"));
    }

    @After
    public void tearDown(){
        userMethods.deleteUser();
    }
}
