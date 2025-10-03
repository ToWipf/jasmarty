package org.wipf.jasmarty;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import org.wipf.jasmarty.logic.base.MainHome;

import io.quarkus.test.junit.QuarkusTest;

/**
 * @author wipf
 *
 */
@QuarkusTest
public class WipfMainTest {

	@Test
	void testMain() {
		System.out.println("Starte Tests");

		System.out.println("Version Check");
		given().when().get("/wipf/ver").then().statusCode(200).body("ver", is(MainHome.VERSION));

		// WipfConfig wc = new WipfConfig().setBy("WipfTesting", "Werte");
		// given().contentType("application/json").body(wc,
		// io.restassured.mapper.ObjectMapperType.JSONB).when().post("/basesettings/save").then().statusCode(200);
	}

}
