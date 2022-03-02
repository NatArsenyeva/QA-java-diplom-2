package user.requests;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserRq {

    private String email;
    private String password;
    private String name;

    public static Faker faker = new Faker();

    public static RegisterUserRq getRandomUser(){
        return new RegisterUserRq(faker.internet().emailAddress(),faker.internet().password(),faker.name().username());
    }

    public static RegisterUserRq getRandomUserWithoutEmail(){
        return new RegisterUserRq(null,faker.internet().password(),faker.name().username());
    }

    public static RegisterUserRq getRandomUserWithoutPassword(){
        return new RegisterUserRq(faker.internet().emailAddress(),null,faker.name().username());
    }

    public static RegisterUserRq getRandomUserWithoutName(){
        return new RegisterUserRq(faker.internet().emailAddress(),faker.internet().password(),null);
    }



}
