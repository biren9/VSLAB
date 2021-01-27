package hska.iwi.eShopMaster.model;

public class ApiConfig {
    private static final String BASE_URL = "http://zuul:8080";
    private static final String API_URI = "/catalog-service";
    private static final String USER_API_URI = "/user-service";
    private static final String AUTHORIZATION_API_URI = "/authorization-service";

    private static final String BASE_URL_API = BASE_URL + API_URI;
    private static final String BASE_URL_USER_API = BASE_URL + USER_API_URI;

    public static final String API_CATEGORIES = BASE_URL_API + "/catalog/categories";
    public static final String API_PRODUCTS = BASE_URL_API + "/catalog/products";

    public static final String USER_API_ROLES = BASE_URL_USER_API + "/roles";
    public static final String USER_API_USERS = BASE_URL_USER_API + "/users";

    public static final String ACCESS_TOKEN_URI = BASE_URL + AUTHORIZATION_API_URI + "/oauth/token";

    public static final String AUTH_ME_URI = BASE_URL + AUTHORIZATION_API_URI + "/self";
}