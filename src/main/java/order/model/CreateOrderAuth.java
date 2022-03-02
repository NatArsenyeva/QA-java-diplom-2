package order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import order.model.OrderAuth;

@Data
@AllArgsConstructor
public class CreateOrderAuth {

    private boolean success;
    private String name;
    private OrderAuth order;

}
