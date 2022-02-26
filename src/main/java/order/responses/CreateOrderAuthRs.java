package order.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import order.model.OrderAuth;

@Data
@AllArgsConstructor
public class CreateOrderAuthRs {

    private boolean success;
    private String name;
    private OrderAuth order;

}
