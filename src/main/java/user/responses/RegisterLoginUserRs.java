package user.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import user.requests.RegisterUserRq;

@Data
@AllArgsConstructor
public class RegisterLoginUserRs {

    private boolean success;
    private String accessToken;
    private String refreshToken;
    private RegisterUserRq user;

}
