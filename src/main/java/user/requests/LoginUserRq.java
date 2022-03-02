package user.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserRq {

    private String email;
    private String password;

    public static LoginUserRq getUserCredentials(RegisterUserRq user){
        return new LoginUserRq(user.getEmail(),user.getPassword());
    }

}
