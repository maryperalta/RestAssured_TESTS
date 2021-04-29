import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class RestAssuredClass {

    private static final String url = "http://demo.guru99.com/V4/sinkministatement.php";
    private static final String CUSTOMER_ID = "68195";
    private static final String PASSWORD = "1234!";
    private static final String ACCOUNT_NO = "1";


    /* L'URL utilisée est longue et moins lisible,
    * 3 paramètres de requête sont utilisés qui sont
    * Customer_ID
    * Password
    * Account_No
    */

    @Test
    public void getResponseBody_all(){
        System.out.println("getResponseBody_all");
        given().when().get("http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1").then().log()
                .all();
    }

    /* Rest Assured, nous aide à passer chaque partie (query, path, header param) séparément,
    * ce qui rend le code plus lisible et facile à maintenir.
    * Pour utiliser les paramètres de requête (query param), tous sont passés en tant que partie du given().
    * l'utilisation de "body" au lieu de "all"; aide à extraire uniquement le corps de la réponse.
    */
    @Test
    public void getResponseBody_body(){
        System.out.println("getResponseBody_body");
         given().queryParam("CUSTOMER_ID", CUSTOMER_ID)
                .queryParam("PASSWORD",PASSWORD)
                .queryParam("Account_No",ACCOUNT_NO)
                .when().get(url).then().log()
                .body();
    }

    /* Obtenir le code d'état de la réponse
    * Obtient le code d'état + assertion pour le valider
    * 200 est une réponse réussie pour ce scénario
    *
    */
    @Test
    public void getResponseStatus_code() {
        System.out.println("getResponseStatus_code");
        int statusCode = given().queryParam("CUSTOMER_ID",CUSTOMER_ID)
                .queryParam("PASSWORD",PASSWORD)
                .queryParam("Account_No",ACCOUNT_NO)
                .when().get(url)
                .getStatusCode();

        System.out.println("La réponse de l'état est " + statusCode);
        given()
                .when()
                .get(url)
                .then().assertThat().statusCode(200);
    }


    /* Scripts pour récupérer différentes parties d'une réponse */

    /*
    Rest Assured est un langage très simple
    la récupération des en-têtes (headers) est simple.
    Le nom de la méthode est headers().
     */
    @Test
    public void getResponse_headers() {
        Headers headers = given().queryParam("CUSTOMER_ID", CUSTOMER_ID)
                .queryParam("PASSWORD", PASSWORD)
                .queryParam("Account_No", ACCOUNT_NO)
                .when()
                .get(url)
                .then().extract().headers();
        System.out.println("Les headers dans la réponse");
        System.out.println(headers);
    }

    /*
    Temps de réponse
    Pour obtenir le temps nécessaire pour récupérer la réponse du backend ou d'autres systèmes en aval,
    Rest Assured fournit une méthode appelée 'timeIn' avec une timeUnit appropriée pour obtenir le temps nécessaire pour renvoyer la réponse.
     */
    @Test
    public void getResponseTime(){
        System.out.println("Le temps pour récupérer la réponse");
        Long temps = given()
                .when()
                .get(url)
                .timeIn(TimeUnit.MILLISECONDS);
        System.out.println(temps);
    }


    /* Récupérer un élément JSON individuel */

    /*
    Rest Assured, fournit un mécanisme pour atteindre les valeurs dans l'API en utilisant "path"
     */
    @Ignore
    @Test
    public void getSpecificPartOfResponseBody(){

        ArrayList<String> montants = given().when().get(url).then().extract().path("result.statements.AMOUNT") ;

        XmlPath xmlPath = given().queryParam("CUSTOMER_ID", CUSTOMER_ID)
                .queryParam("PASSWORD",PASSWORD)
                .queryParam("Account_No",ACCOUNT_NO)
                .when().get(url).then()
                .extract().body().htmlPath();
                System.out.println("La valeur du montant récupéré est : "+ xmlPath.prettyPrint());

/*        XmlPath zz = given().queryParam("CUSTOMER_ID", CUSTOMER_ID)
                .queryParam("PASSWORD",PASSWORD)
                .queryParam("Account_No",ACCOUNT_NO)
                .when().get(url).then()
                .extract().body().path("result.statements.AMOUNT");*/

                System.out.println("xmlPath : " + xmlPath.get("'result:'.statements[1]"));

//        System.out.println("La valeur du montant récupéré est : "+ xx.prettyPrint());
        int somme = 0;
        /*for(String m:montants){

            System.out.println("La valeur du montant récupéré est : "+m);
            somme += Integer.valueOf(m);
        }*/
//        System.out.println("Le montant total est "+given().when().get(url).then().extract().path("result.statements.AMOUNT"));

    }


}
