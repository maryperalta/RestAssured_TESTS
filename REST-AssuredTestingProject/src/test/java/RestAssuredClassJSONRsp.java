import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class RestAssuredClassJSONRsp {

    /* Comment analyser la réponse JSON */
    /* JSONPlaceholder qui est une fausse API REST en ligne pour les tests et le prototypage.
     * JSONPlaceholder est un service REST en ligne gratuit que vous pouvez utiliser chaque fois que vous avez besoin de fausses données.
     */
    private static final String endpoint = "https://jsonplaceholder.typicode.com/users";

    private Response response;
    private List<String> jsonResponse;

    @Before
    public void preparation() {
        response = doGetRequest(endpoint);
        jsonResponse = response.jsonPath().getList("$");
    }

    public static Response doGetRequest(String endpoint) {
        RestAssured.defaultParser = Parser.JSON;
        return
                given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().get(endpoint).
                        then().contentType(ContentType.JSON).extract().response();
    }

    @Test
    public void makeSureThatServerIsUp() {
        System.out.println("makeSureThatServerIsUp");
        given().when().get(endpoint).then().statusCode(200);
    }

    /*
    Le premier test consiste généralement à compter le nombre d’enregistrements dans le tableau, alors commençons par cela.
     */
    @Test
    public void doGetRequest_reponse_size() {
        int size = jsonResponse.size();
        System.out.println(size == 10);
    }

    /*
    Obtenir le nom d'utilisateur de toutes les entrées
     */
    @Test
    public void doGetRequest_get_usernames() {
        String usernames = response.jsonPath().getString("username");
        System.out.println(usernames);
    }

    /*

     */
    @Test
    public void doGetRequest_get_entreprise_3_facons() {

        // L'entreprise est en fait une Map.
        // Si la réponse renvoie un tableau et que nous voulons extraire le premier nom de la société, nous pourrions utiliser:
        Map<String, String> companies = response.jsonPath().getMap("company[0]");
        System.out.println("tableau 0 => " + companies.get("name"));

        List<Map<String, String>> companies0 = response.jsonPath().getList("company");
        System.out.println("get 0 => " + companies0.get(0).get("name"));

        // Si nous n'avions qu'un seul enregistrement, nous pourrions utiliser
        response = doGetRequest(endpoint + "/1");
        Map<String, String> company = response.jsonPath().getMap("company");
        System.out.println("Map company => " + company);
        System.out.println("get first => " + company.get("name"));

    }

    @Test
    public void getResponseBody() {
        System.out.println("getResponseBody_body");
        given().when().get(endpoint).then().log()
                .body();
    }

    /* Obtenir le code d'état de la réponse
     * Obtient le code d'état + assertion pour le valider
     * 200 est une réponse réussie pour ce scénario
     */
    @Test
    public void getResponseStatus_code() {
        System.out.println("getResponseStatus_code");
        int statusCode = given()
                .when().get(endpoint)
                .getStatusCode();

        System.out.println("La réponse de l'état est " + statusCode);
        given()
                .when()
                .get(endpoint)
                .then().assertThat().statusCode(200);
    }

    @Test
    public void getResponse_all_zipCodes_values() {
        ArrayList<String> zipcodeList = given().when().get(endpoint).then().extract().path("address.zipcode");
        System.out.println("zipcodeList " + zipcodeList);
    }

    @Test
    public void getResponse_assert_zipcode() {
        System.out.println("getResponse_assert_name");
        given().when().get(endpoint + "/1")
                .then().statusCode(200)
                .assertThat()
                .body("address.zipcode", equalTo("92998-3874"))
                .log().everything();
    }

    @Test
    public void getResponse_assert_erreur_404_not_found() {
        System.out.println("getResponse_assert_erreur_404_not_found");
        given().when().get(endpoint + "/50000")
                .then().statusCode(404)
                .assertThat()
                .body("address.zipcode", equalTo(null))
                .log().everything();
    }

    @Test
    public void getResponse_has_key() {
        System.out.println("getResponse_has_key");
        given().when().get(endpoint + "/1")
                .then().statusCode(200)
                .assertThat()
                .body("company", hasKey("catchPhrase"))
                .log().everything();

    }

    @Ignore
    @Test
    public void get_erreur_404() {
        System.out.println("getResponse_assert_erreur_404");
        given().when().get("/50000")
                .then().log().everything();
    }

}
