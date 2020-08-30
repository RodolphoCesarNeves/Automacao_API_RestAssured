package br.rdc.rest.api;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class UserJsonTest {
	
	@BeforeClass
	public static void setup() 
	{
		RestAssured.baseURI="https://restapi.wcaquino.me";
//		RestAssured.port=80;
//		RestAssured.basePath="/v2";
		
	}
	
	@Test
	public void deveVerificarPrimriroNivel() 
	{		
		given()
			.log().all()
		.when()
			.get("/users/1")
		//.get("https://restapi.wcaquino.me/users/1")
		.then()
		.statusCode(200)
		.body("id", is(1))
		.body("name", containsString("Silva"))
		.body("age",greaterThan(18));

}
	
//	{
//		  "id": 1,
//		  "name": "João da Silva",
//		  "age": 30,
//		  "salary": 1234.5678
//		}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() 
	{
		Response response= RestAssured.request(Method.GET,"/users/1");
		
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
	
//	{
//		  "id": 2,
//		  "name": "Maria Joaquina",
//		  "endereco": {
//		    "rua": "Rua dos bobos",
//		    "numero": 0
//		  },
//		  "age": 25,
//		  "salary": 2500
//		}
	
	@Test
	public void deveVerificarSegundoNivel() 
	{
		given()
		.when()
			.get("/users/2")
		.then()
			.statusCode(200)		
			.body("name", containsString("Joaquina"))
			.body("endereco.rua",is("Rua dos bobos"));

}
	
//	{
//		  "id": 3,
//		  "name": "Ana Júlia",
//		  "age": 20,
//		  "filhos": [
//		    {
//		      "name": "Zezinho"
//		    },
//		    {
//		      "name": "Luizinho"
//		    }
//		  ]
//		}
	
	@Test
	public void deveverificarLista() 
	{
		given()
		.when()
			.get("/users/3")
		.then()
			.statusCode(200)		
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))//verifica se a lista passui dois elementos
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos[1].name", equalToIgnoringCase("luizinho"))
			.body("filhos.name", hasItem("Luizinho"))			
			.body("filhos.name",hasItems("Zezinho","Luizinho"))

	;}
	
	@Test
	public void deveRetornarErroUsuarioInexistente() 
	{
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error",is("Usuário inexistente"))
		
	;}
	
	@Test
	public void deveVerificarLstaRaiz() 
	{
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("", hasSize(3))
			.body("name", hasItems("João da Silva","Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null))
	;}
	
	
//	[
//	  {
//	    "id": 1,
//	    "name": "João da Silva",
//	    "age": 30,
//	    "salary": 1234.5678
//	  },
//	  {
//	    "id": 2,
//	    "name": "Maria Joaquina",
//	    "endereco": {
//	      "rua": "Rua dos bobos",
//	      "numero": 0
//	    },
//	    "age": 25,
//	    "salary": 2500
//	  },
//	  {
//	    "id": 3,
//	    "name": "Ana Júlia",
//	    "age": 20,
//	    "filhos": [
//	      {
//	        "name": "Zezinho"
//	      },
//	      {
//	        "name": "Luizinho"
//	      }
//	    ]
//	  }
//	]
	
	@Test
	public void deveFazerVerificacoesAvancadas() 
	{
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("", hasSize(3))
			.body("age.findAll{it <=25}.size()", is(2))// o "it" vai ser a instância atual da idade (age) e verificar se tem dois registros com idade maior ou igual a 25 (is (2))
			.body("age.findAll{it <=25 && it>20}.size()", is(1))
			.body("findAll{it.age <=25 && it.age>20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <=25}[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <=25}[-1].name", is("Ana Júlia"))
			.body("find{it.age <=25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name",hasItems("Maria Joaquina","Ana Júlia"))
			.body("findAll{it.name.length()>10}.name",hasItems("João da Silva","Maria Joaquina"))
			.body("name.collect{it.toUpperCase()}",hasItems("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",hasItems("MARIA JOAQUINA"))
			.body("age.collect{it*2}",hasItems(60,50,40))
			.body("id.max()",is(3))
			.body("salary.min()",is(1234.5678f))
			.body("salary.findAll{it !=null}.sum()",is(closeTo(3734.5678f,0.001)))				
			
			;}
	
	@Test
	public void devoUnirJsonPathComJAVA() 
	{	ArrayList<String> names=
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.extract().path("name.findAll{it.startsWith('Maria')}");
	
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("maRia joaQuina"));
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
		
	}
}