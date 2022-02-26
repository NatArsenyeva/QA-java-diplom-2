package common.config;

import org.aeonbits.owner.Config;

@Config.Sources("file:src/main/resources/api-config.properties")
public interface ApiConfig extends Config {

    @Key("base.url")
    String getBaseUrl();

    @Key("api.auth.register.url")
    String getApiRegisterUrl();

    @Key("api.auth.login.url")
    String getApiLoginUrl();

    @Key("api.auth.user.url")
    String getApiUserUrl();

    @Key("api.auth.orders.url")
    String getApiOrdersUrl();

    @Key("api.auth.ingredients.url")
    String getApiIngredientsUrl();


}
