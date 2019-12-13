import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BasicTest {

    public static String authToken;

    @Test(priority = 0)
    public void testStatusCode() {
        given()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products")
                .then()
                .statusCode(200);

    }

    @Test (priority = 1)
    public void testLogging() {
        given()
                .log().all()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

    }

    @Test (priority = 2)
    public void printResponse(){
        Response res = given().when()
                        .log().all()
                        .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
        //System.out.println(res.asString());
        //System.out.println("********************************");
        System.out.println(res.prettyPrint());

    }

    @Test(priority = 3)
    public void testCurrency() {
        Response res=given()
                    .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
        JsonPath jsonPathEvaluator = res.jsonPath();
        List<Map> products = jsonPathEvaluator.getList("data");
        for(Map product : products)
        {
            Map attributes = (Map)product.get("attributes");
            System.out.println("Product ID::"+ product.get("id") + " Currency::" + attributes.get("currency"));
            Assert.assertEquals(attributes.get("currency"), "USD");
            //Assert.assertTrue(attributes.get(("currency")).equals("USD");
        }

    }
    @Test(priority = 4)
    public void testFilter() {
        Response res = given()
                .log().all()
                .queryParam("filter[name]", "bag")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

          System.out.println(res.prettyPrint());

    }

    @Test(priority = 5)
    public void testFilterIds() {
        Response res = given()
                .log().all()
                .queryParam("filter[ids]", "1")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

        System.out.println(res.prettyPrint());

    }
    @Test(priority = 6)
    public void testFilterPrice() {
        Response res = given()
                .log().all()
                .queryParam("filter[price]", "19.99")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
        System.out.println(res.prettyPrint());
    }

    @Test(priority = 7)
    public void testFilterOptionsColour() {
        Response res = given()
                .log().all()
                .queryParam("filter[options][tshirt-color]", "S","Red")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
        System.out.println(res.prettyPrint());
    }
    @BeforeClass
    public void authToken() {
        Response res = given()
                .formParam("grant_type","password")
                .formParam("username", "shwethapissay@gmail.com")
                .formParam("password", "pass123")
                .post("https://spree-vapasi-prod.herokuapp.com/spree_oauth/token");

        authToken = "Bearer " + res.path("access_token");
    }

    @Test
    public void testPostCall(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authToken);

        String createBody = "{\n" +
                "  \"variant_id\": \"17\",\n" +
                "  \"quantity\": 5\n" +
                "}";

        Response res = given()
                    .headers(headers)
                    .body(createBody)
                    .when()
                    .post("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/add_item");

        Assert.assertEquals(res.statusCode(), 200);
    }

}