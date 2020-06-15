package br.rdc.rest.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testOlaMundo() {
	Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
	Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
	Assert.assertTrue(response.statusCode()==200);
	Assert.assertTrue("O status deveria ser 200", response.statusCode()==200);
	Assert.assertEquals(200, response.statusCode());
	
	ValidatableResponse validacao= response.then();
	validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() 
	{
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao= response.then();
		validacao.statusCode(200);
		
		get("http://restapi.wcaquino.me/ola").then();
		
		//Given() dado/ When() quando/ Then() então
		
		given()// pré- condição
		.when()// Ação
			.get("http://restapi.wcaquino.me/ola")
		.then()// Assertivas
			.statusCode(200);}
		
	@Test
	public void devoConhecerMatchersHamcrest() 
	{
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128)); //Validar o valor (128=128?)
		Assert.assertThat(128, Matchers.isA(Integer.class));// Validar o tipo de dado (128 é inteiro?)
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(128, Matchers.greaterThan(120));//Validar se 128 será MAIOR que 120
		Assert.assertThat(128, Matchers.lessThan(130));//Validar se 128 será MENOR que 130
		
		List<Integer> impares= Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));//Validar o tamanho da lista de Arrays
		assertThat(impares, contains(1,3,5,7,9));//Validar se a lista contém os ítens (1,3,5,7,9)
		assertThat(impares, containsInAnyOrder(1,3,5,9,7));;//Validar os ítens da lista sem importar a ordem
		assertThat(impares, hasItem(1));//Validar apenas um ítem da lista
		assertThat(impares, hasItems(1,3));// Validar mais de um ítem da lista 
		
		
		assertThat("Maria", is(not("João")));
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("Maria"),is("João")));// Verifica se é Maria OU João
		assertThat("Catarina", allOf(startsWith("Cat"), endsWith("ina"),containsString("ar")));//Valida se o nome se inicia com "Cat", termina com "ina" e se contém "ar"
	}	
	
	@Test
	public void devoValidarBody() 
	{
		
		given()// pré- condição
		.when()// Ação
			.get("http://restapi.wcaquino.me/ola")
		.then()// Assertivas
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
		
		
		
	}
			
		

}


