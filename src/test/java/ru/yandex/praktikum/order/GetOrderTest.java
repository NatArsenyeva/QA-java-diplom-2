package ru.yandex.praktikum.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderMethods;
import order.requests.CreateOrderRq;
import order.model.CreateOrderAuth;
import order.responses.GetOrderAuthRs;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserMethods;
import user.requests.LoginUserRq;
import user.requests.RegisterUserRq;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetOrderTest {

    OrderMethods orderMethods;
    UserMethods userMethods;

    @Before
    public void init() {
        orderMethods = new OrderMethods();
        userMethods = new UserMethods();
    }

    @DisplayName("Ошибка получения заказа без авторизации.")
    @Test
    public void testGetOrderWithoutAuthorizationReturnError() {
        ValidatableResponse response = orderMethods.getOrder();
        response.assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message",is("You should be authorised"));
    }

    @DisplayName("Получение заказа под авторизованным пользователем.")
    @Test
    public void testGetOrderWithAuthorizationReturnOrder() {
        RegisterUserRq user = RegisterUserRq.getRandomUser();
        assertThat(userMethods.isRegistered(user),is(true));
        String token = userMethods.isLogged(LoginUserRq.getUserCredentials(user)).getAccessToken();
        CreateOrderRq orderRq = new CreateOrderRq(orderMethods.getIngredients());
        CreateOrderAuth order = orderMethods.createOrder(orderRq,token)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(CreateOrderAuth.class);
        GetOrderAuthRs response = orderMethods.getOrder(token)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(GetOrderAuthRs.class);
        assertThat(response.isSuccess(),is(true));
        assertThat(response.getOrders().get(0).get_id(),is(order.getOrder().get_id()));
    }

    @After
    public void tearDown(){
        userMethods.deleteUser();
        orderMethods.deleteOrder();
    }

}
