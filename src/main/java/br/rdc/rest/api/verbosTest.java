package br.rdc.rest.api;

import static io.restassured.RestAssured.given;
import  io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

//import com.sun.istack.NotNull;


public class verbosTest {
	
	@Test
	public void deveSalvarUsuario() 
	{
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\":\"Jose\", \"age\":50}")			
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id",is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		
		;
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() 
	{
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\":50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		
		;
	}
	
	@Test
	public void deveSalvarUsuarioXml() 
	{
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")			
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id",is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))		
			
		
		;
	}
	
	@Test
	public void deveAlterarUsuario() 
	{
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"Usuario Alterado\", \"age\":65}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(65))
			.body("salary", is(1234.5678f))
			
		
		;
	}
	
	@Test
	public void devoCustomizarURLParte1() 
	{
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"Usuario Alterado\", \"age\":65}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(65))
			.body("salary", is(1234.5678f))
			
		
		;
	}
	
	@Test
	public void devoCustomizarURLParte2() 
	{
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"Usuario Alterado\", \"age\":65}")
			.pathParams("entidade", "users")
			.pathParams("userId", "1")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(65))
			.body("salary", is(1234.5678f))
			
		
		;
	}
	
	@Test
	public void deveRemoverUsuario() 
	{
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
	;}
	
	@Test
	public void naoDeveremoverUsuarioInexistente() 
	{
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		
		
	;}


}
