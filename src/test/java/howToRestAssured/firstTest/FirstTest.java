package howToRestAssured.firstTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;


@RunWith(Parameterized.class)
public class FirstTest {
	private static final String YOUR_AUTHENTICATION_TOKEN_GOES_HERE = null;

	@Test
	public void makeSureThatGoogleIsUp() {
		given().
		when().
		get("http://www.google.com").
		then().
		statusCode(200);
	}

	@Test
	public void test_NumberOfCircuitsFor2017Season_ShouldBe20() {
	        
	    given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(20));
	}
	
	@Test
	public void test_ResponseHeaderData_ShouldBeCorrect() {
	        
	    given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        statusCode(200).
	    and().
	    	contentType(ContentType.JSON);
	}
	
	@Test
	public void test_NumberOfCircuits_ShouldBe20_Parameterized() {
	        
	    String season = "2017";
	    int numberOfRaces = 20;
	        
	    given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(numberOfRaces));
	}
	
	@Parameters
	public static Object[][] createTestDataRecords() {
	    return new Object[][] {
	        {"2017",20},
	        {"2016",21},
	        {"1966",9}
	    };
	}
	
	private String season;

    private int numberOfRaces;

    public FirstTest(String season, int numberOfRaces) {
        this.season = season;
        this.numberOfRaces = numberOfRaces;
    }	
	
	@Test
	public void test_NumberOfCircuits_ShouldBe_DataDriven() {
	                
	    given().
	        pathParam("raceSeason",season).
	    when().
	        get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(numberOfRaces));
	}
	
	
	//Secure Test API
	
	@Test
	public void test_APIWithBasicAuthentication_ShouldBeGivenAccess() {
	        
	    given().
	        auth().
	        preemptive().
	        basic("username", "password").
	    when().
	        get("http://path.to/basic/secured/api").
	    then().
	        assertThat().
	        statusCode(200);
	}
	
//	@Test
//	public void test_APIWithOAuth2Authentication_ShouldBeGivenAccess() {
//	        
//	    given().
//	        auth().
//				oauth2(YOUR_AUTHENTICATION_TOKEN_GOES_HERE).	    
//				when().
//	        get("http://path.to/oath2/secured/api").
//	    then().
//	        assertThat().
//	        statusCode(200);
//	}
	
	//Passing parameters between tests
	
	@Test
	public void test_ScenarioRetrieveFirstCircuitFor2017SeasonAndGetCountry_ShouldBeAustralia() {
	        
	    // First, retrieve the circuit ID for the first circuit of the 2017 season
	    String circuitId = given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        extract().
	        path("MRData.CircuitTable.Circuits.circuitId[0]");
	        
	    // Then, retrieve the information known for that circuit and verify it is located in Australia
	    given().
	        pathParam("circuitId",circuitId).
	    when().
	        get("http://ergast.com/api/f1/circuits/{circuitId}.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.Location[0].country", equalTo("Australia"));
	}
	
	//Reusing checks with ResponseSpecBuilder
	
	ResponseSpecification checkStatusCodeAndContentType = 
		    new ResponseSpecBuilder().
		        expectStatusCode(200).
		        expectContentType(ContentType.JSON).
		        build();

	@Test
	public void test_NumberOfCircuits_ShouldBe20_UsingResponseSpec() {
	        
	    given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        assertThat().
	        spec(checkStatusCodeAndContentType).
	    and().
	        body("MRData.CircuitTable.Circuits.circuitId",hasSize(20));
	}
	
}
