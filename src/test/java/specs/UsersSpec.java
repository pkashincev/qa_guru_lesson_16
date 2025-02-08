package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.emptyOrNullString;

public class UsersSpec {
    public static RequestSpecification withBodyRequestSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().body()
            .log().headers()
            .contentType(JSON);

    public static RequestSpecification noBodyRequestSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().headers();

    public static ResponseSpecification successfulResponseSpec = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .log(LogDetail.HEADERS)
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification notFoundResponseSpec = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .log(LogDetail.HEADERS)
            .expectStatusCode(404)
            .build();

    public static ResponseSpecification createdResponseSpec = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .log(LogDetail.HEADERS)
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification noContentResponseSpec = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .log(LogDetail.HEADERS)
            .expectStatusCode(204)
            .expectBody(emptyOrNullString())
            .build();
}
