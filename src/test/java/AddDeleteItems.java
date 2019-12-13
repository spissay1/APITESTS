import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class AddDeleteItems {

    public static String authToken;

    @BeforeClass
    public void authToken() {
        Response res = given()
                .formParam("grant_type", "password")
                .formParam("username", "shwethapissay@gmail.com")
                .formParam("password", "pass123")
                .post("https://spree-vapasi-prod.herokuapp.com/spree_oauth/token");

        authToken = "Bearer " + res.path("access_token");
    }

    @Test(priority = 0)
    public void getProducts() {
        Response res = given()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
        System.out.println(res.jsonPath().prettify());

    }

    @Test(priority = 1)
    public void testAddItem() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authToken);

        String createBody = "{\n" +
                "  \"variant_id\": \"17\",\n" +
                "  \"quantity\": 1\n" +
                "}";

        Response res = given()
                .headers(headers)
                .body(createBody)
                .when()
                .post("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/add_item");

        Assert.assertEquals(res.statusCode(), 200);
    }

    @Test(priority = 2)
    public void viewCart() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authToken);

        Response res = given()
                .headers(headers)
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart");
        System.out.println(res.jsonPath().prettify());

    }

    @Test(priority = 3)
    public void deleteItem() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authToken);
        Response res = given()
                .headers(headers)
                .when()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart");

        JsonPath jsonPathEvaluator = res.jsonPath();
        List<Map<String, String>> lineItems = jsonPathEvaluator.getList("data.relationships.line_items.data");
        for (Map<String, String> lineitem : lineItems) {
            String id = lineitem.get("id");
            System.out.println("id::" + id);
            Response deleteRes = given()
                    .headers(headers)
                    .when()
                    .delete("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/remove_line_item/" + id);
            Assert.assertEquals(deleteRes.statusCode(), 200);
        }
    }
}
