package order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Owner {

    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
}
