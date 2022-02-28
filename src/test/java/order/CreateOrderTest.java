package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.xml.XmlPath;
import order.requests.CreateOrderRq;
import order.responses.CreateOrderNotAuthRs;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserMethods;
import user.requests.LoginUserRq;
import user.requests.RegisterUserRq;
import common.ErrorRs;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {

    OrderMethods orderMethods;
    UserMethods userMethods;

    @Before
    public void init() {
        orderMethods = new OrderMethods();
        userMethods = new UserMethods();
    }

    @DisplayName("Создание заказа с ингредиентами под авторизованным пользователем.")
    @Test
    public void testOrderCreatedWithAuthorizationWithIngredients() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        assertThat(userMethods.isRegistered(user),is(true));
        String token = userMethods.isLogged(LoginUserRq.getUserCredentials(user)).getAccessToken();
        CreateOrderRq orderRq = new CreateOrderRq(orderMethods.getIngredients());
        CreateOrderNotAuthRs response = orderMethods.createOrder(orderRq,token)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(CreateOrderNotAuthRs.class);
        assertThat(response.isSuccess(),is(true));
        assertThat(response.getName(),is(notNullValue()));
        assertThat(response.getOrder().getNumber(),is(notNullValue()));
    }

    @DisplayName("Создание заказа с ингредиентами без авторизации.")
    @Test
    public void testOrderCreatedWithoutAuthorizationWithIngredients() {
        CreateOrderRq orderRq = new CreateOrderRq(orderMethods.getIngredients());
        CreateOrderNotAuthRs response = orderMethods.createOrder(orderRq)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(CreateOrderNotAuthRs.class);
        assertThat(response.isSuccess(),is(true));
        assertThat(response.getName(),is(notNullValue()));
        assertThat(response.getOrder().getNumber(),is(notNullValue()));
    }

    @DisplayName("Ошибка при создании заказа без ингредиентов под авторизованным пользователем.")
    @Test
    public void testOrderNotCreatedWithAuthorizationWithoutIngredients() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        assertThat(userMethods.isRegistered(user),is(true));
        String token = userMethods.isLogged(LoginUserRq.getUserCredentials(user)).getAccessToken();
        CreateOrderRq orderRq = new CreateOrderRq(null);
        ErrorRs response = orderMethods.createOrder(orderRq,token)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("Ingredient ids must be provided"));
    }

    @DisplayName("Ошибка при создании заказа без ингредиентов и авторизации.")
    @Test
    public void testOrderNotCreatedWithoutAuthorizationWithoutIngredients() {
        CreateOrderRq order = new CreateOrderRq(null);
        ErrorRs response = orderMethods.createOrder(order)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .as(ErrorRs.class);
        assertThat(response.isSuccess(),is(false));
        assertThat(response.getMessage(),is("Ingredient ids must be provided"));
    }

    @DisplayName("Ошибка при создании заказа с неверным хеш-кодом ингредиента.")
    @Test
    public void testOrderNotCreatedWithIncorrectHashCodeIngredient() {
        CreateOrderRq order = new CreateOrderRq(List.of("hashCode1,hashCode2"));
        XmlPath response = orderMethods.createOrder(order)
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .extract()
                .path("html");
        assertThat(response.get("html.body.pre"), is("Internal Server Error"));
    }

    @After
    public void tearDown(){
        userMethods.deleteUser();
        orderMethods.deleteOrder();
    }

}











