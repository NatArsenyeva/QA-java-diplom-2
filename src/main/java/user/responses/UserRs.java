package user.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import user.requests.UpdateUserRq;

@Data
@AllArgsConstructor
public class UserRs {
    private boolean success;
    private UpdateUserRq user;

}
