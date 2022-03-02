package order;

import common.base.BaseSpec;
import common.config.ApiConfig;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.requests.CreateOrderRq;
import org.aeonbits.owner.ConfigFactory;
import user.Tokens;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderMethods extends BaseSpec {

    public static final String ORDER_PATH = ConfigFactory.create(ApiConfig.class).getApiOrdersUrl();
    public static final String INGREDIENTS_PATH = ConfigFactory.create(ApiConfig.class).getApiIngredientsUrl();

    @Step("Отправить GET запрос на получение списка ингредиентов.")
    public List<String> getIngredients(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .extract().path("data._id");
    }

    @Step("Отправить POST запрос без токена на создание заказа.")
    public ValidatableResponse createOrder(CreateOrderRq order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Отправить POST запрос с токеном на создание заказа.")
    public ValidatableResponse createOrder(CreateOrderRq order, String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Отправить GET запрос без токена на получение заказов.")
    public ValidatableResponse getOrder(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Отправить GET запрос с токеном на получение заказов.")
    public ValidatableResponse getOrder(String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Отправить DELETE запрос на удаление заказа.")
    public void deleteOrder() {
        if (Tokens.getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete(ORDER_PATH)
                .then()
                .statusCode(202);
    }


}
