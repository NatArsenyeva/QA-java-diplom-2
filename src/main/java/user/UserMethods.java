package user;

import common.base.BaseSpec;
import common.config.ApiConfig;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.aeonbits.owner.ConfigFactory;
import org.apache.http.HttpStatus;
import user.requests.LoginUserRq;
import user.requests.UpdateUserRq;
import user.requests.RegisterUserRq;
import user.responses.RegisterLoginUserRs;

import static io.restassured.RestAssured.given;

public class UserMethods extends BaseSpec {

    public static final String REGISTER_PATH = ConfigFactory.create(ApiConfig.class).getApiRegisterUrl();
    public static final String LOGIN_PATH = ConfigFactory.create(ApiConfig.class).getApiLoginUrl();
    public static final String USER_PATH = ConfigFactory.create(ApiConfig.class).getApiUserUrl();

    @Step("Отправить POST запрос на регистрацию пользователя.")
    public ValidatableResponse register(RegisterUserRq user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Отправить POST запрос на регистрацию и проверить значение 'success'.")
    public boolean isRegistered(RegisterUserRq user){
        return register(user)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("success")
                .equals(true);
    }

    @Step("Отправить POST запрос на авторизацию пользователя.")
    public ValidatableResponse login(LoginUserRq login){
        return given()
                .spec(getBaseSpec())
                .body(login)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Отправить POST запрос на авторизацию пользователя и вернуть ответ на запрос.")
    public RegisterLoginUserRs isLogged(LoginUserRq login){
        return login(login)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(RegisterLoginUserRs.class);
    }

    @Step("Отправить PATCH запрос с токеном на обновление пользователя.")
    public ValidatableResponse updateUserData(UpdateUserRq userData, String token)  {
           return given()
                   .spec(getBaseSpec())
                   .auth().oauth2(token.substring(7))
                   .body(userData)
                   .patch(USER_PATH)
                   .then();
    }

    @Step("Отправить PATCH запрос без токена на обновление пользователя.")
    public ValidatableResponse updateUserData(UpdateUserRq userData)  {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .patch(USER_PATH)
                .then();
    }

    @Step("Отправить DELETE запрос на удаление пользователя.")
    public void deleteUser() {
        if (Tokens.getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete(USER_PATH)
                .then()
                .statusCode(202);
    }

}
