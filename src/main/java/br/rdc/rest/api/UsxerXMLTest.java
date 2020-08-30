package br.rdc.rest.api;


import static org.hamcrest.Matchers.*;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;

public class UsxerXMLTest {
	

/*<user id="3">
	<name>Ana Julia</name>
		<age>20</age>
		<filhos>
			<name>Zezinho</name>
			<name>Luizinho</name>
		</filhos>
</user>*/
	
	@Test
	
	public void devotrabalharComXLM() 
	{
		given()
		.when()
			.get("https://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.body("user.name",is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()", is(2))
			.body("user.filhos.name[0]",is("Zezinho"))
			.body("user.filhos.name[1]",is("Luizinho"))
			.body("user.filhos.name",hasItem("Luizinho"))
			.body("user.filhos.name",hasItems("Luizinho","Zezinho"))
		;
	}
	
	@Test
	
	public void devoTrabalharcomXMLUsandoRootPath() 
	{
		given()
		.when()
			.get("https://restapi.wcaquino.me/usersXML/3")
		.then()
		.statusCode(200)
		
		.rootPath("user")
		.body("name",is("Ana Julia"))
		.body("@id", is("3"))
		
		.rootPath("user.filhos")
		.body("name.size()", is(2))
		
		.detachRootPath("filhos")
		.body("filhos.name[0]",is("Zezinho"))		
		.body("filhos.name[1]",is("Luizinho"))
		
		.appendRootPath("filhos")
		.body("name",hasItem("Luizinho"))
		.body("name",hasItems("Luizinho","Zezinho"));
			
	}
	
	/*
	 <users>
		<user id="1">
			<name>João da Silva</name>
			<age>30</age>
			<salary>1234.5678</salary>
		</user>
		<user id="2">
			<name>Maria Joaquina</name>
			<age>25</age>
			<salary>2500</salary>
			<endereco>
				<rua>Rua dos bobos</rua>
				<numero>0</numero>
			</endereco>
		</user>
		<user id="3">
			<name>Ana Julia</name>
			<age>20</age>
			<filhos>
				<name>Zezinho</name>
				<name>Luizinho</name>
			</filhos>
		</user>
	</users>
*/
	
	@Test
	
	public void devoFazerPesquisasAvancadascomXML() 
	{
		given()
		.when()
		.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger()<=25}.size()", is(2))
			.body("users.user.@id", hasItems("1","2","3"))
			.body("users.user.find{it.age==25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it !=null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger()*2}", hasItems(40,50,60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
			
			;
	}

}
