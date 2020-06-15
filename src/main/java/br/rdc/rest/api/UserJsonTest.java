package br.rdc.rest.api;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class UserJsonTest {
	
	@Test
	public void deveVerificarPrimriroNivel() 
	{
		given()
		.when()
		.get("https://restapi.wcaquino.me/users/1")
		.then()
		.statusCode(200)
		.body("id", is(1))
		.body("name", containsString("Silva"))
		.body("age",greaterThan(18));

}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() 
	{
		Response response= RestAssured.request(Method.GET,"https://restapi.wcaquino.me/users/1");
		
		//path
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s","id"));
		
		//JsonPath
		JsonPath jpath= new JsonPath(response.asString());
		Assert.assertEquals(1,jpath.getInt("id"));
		
		//from
		
		int id= JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1,id);
	}
}