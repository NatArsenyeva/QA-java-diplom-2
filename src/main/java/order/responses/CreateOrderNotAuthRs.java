package order.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import order.model.OrderNotAuth;

@Data
@AllArgsConstructor
public class CreateOrderNotAuthRs {

    private boolean success;
    private String name;
    private OrderNotAuth order;

}
