package order;

import common.base.BaseSpec;
import common.config.ApiConfig;
import io.restassured.response.ValidatableResponse;
import order.requests.CreateOrderRq;
import org.aeonbits.owner.ConfigFactory;
import user.Tokens;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderMethods extends BaseSpec {

    public static final String ORDER_PATH = ConfigFactory.create(ApiConfig.class).getApiOrdersUrl();
    public static final String INGREDIENTS_PATH = ConfigFactory.create(ApiConfig.class).getApiIngredientsUrl();

    public List<String> getIngredients(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .extract().path("data._id");
    }

    public ValidatableResponse createOrder(CreateOrderRq order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse createOrder(CreateOrderRq order, String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrder(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrder(String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .get(ORDER_PATH)
                .then();
    }

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
