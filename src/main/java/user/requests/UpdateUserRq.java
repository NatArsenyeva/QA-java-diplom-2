package user.requests;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRq {

    private String email;
    private String name;

    public static UpdateUserRq getRandomUserData(){
        Faker faker = new Faker();
        return new UpdateUserRq(faker.internet().emailAddress(),faker.name().username());
    }

}
