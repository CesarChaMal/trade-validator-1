package priv.rdo.trade.endpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static priv.rdo.trade.helper.TestFileUtils.fileToString;

/**
 * @author WrRaThY
 * @since 09.07.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TradeValidationEndpointSystemTest {

    @LocalServerPort
    private Integer port;


    @Test
    public void tradesOk() throws Exception {
        //@formatter:off
        given()
                .port(port)
                .log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(fileToString("spot_ok.json"))
        .when()
                .post("/trades")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(OK.value())
                    .body("validationStatus", is("SUCCESS"));
        //@formatter:off
    }

    @Test
    public void tradesBadTypeCurrencyPairCustomerAndLegalEntity() throws Exception {
        //@formatter:off
        given()
                .port(port)
                .log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(fileToString("very_bad.json"))
        .when()
                .post("/trades")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(BAD_REQUEST.value())
                    .body("validationStatus", is("FAILURE"))
                    .body("errors", hasSize(4))
                    .body("errors.message", contains("currency value is invalid", "We do not support a customer named PLUTO13",
                            "We do not support an named CS Zurichc", "Type has to be one of: [Spot, Forward, VanillaOption]"))
                ;
        //@formatter:off
    }

    @Test
    public void tradesCannotOccurDuringHolidays() throws Exception {
        //@formatter:off
        given()
                .port(port)
                .log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(fileToString("spot_nok_4th_of_July.json"))
        .when()
                .post("/trades")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(BAD_REQUEST.value())
                    .body("validationStatus", is("FAILURE"))
                    .body("errors", hasSize(1))
                    .body("errors.message", contains("the value date should be exactly two days after trade date"))
                ;
        //@formatter:off
    }

    @Test
    public void tradesBulkMultipleErrorsInDifferentOptionsAndSuccess() throws Exception {
        //@formatter:off
        given()
                .port(port)
                .log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(fileToString("option_bulk_2bad_1good.json"))
        .when()
                .post("/bulkTrades")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(BAD_REQUEST.value())
                    .body("results.validationStatus", hasItems("FAILURE", "FAILURE", "SUCCESS"))
                    .body("results.errors[0].message", contains("currency value is invalid", "the exerciseStartDate has to be after the tradeDate and before the expiryDate"))
                    .body("results.errors[1].message", contains("We do not support a customer named PLUTO3", "the expiry date and the premium date shall be before the delivery date"))
                ;
        //@formatter:off
    }

}