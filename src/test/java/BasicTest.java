import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BasicTest {

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
        }

    }
}