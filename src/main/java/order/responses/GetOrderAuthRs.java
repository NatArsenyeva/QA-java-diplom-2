package order.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import order.model.GetOrderAuth;

import java.util.List;

@Data
@AllArgsConstructor
public class GetOrderAuthRs {

    private boolean success;
    private List<GetOrderAuth> orders;
    private int total;
    private int totalToday;

}
