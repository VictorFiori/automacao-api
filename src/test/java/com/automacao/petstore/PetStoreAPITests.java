package com.automacao.petstore;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.automacao.petstore.models.Order;
import com.automacao.petstore.models.OrderStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PetStoreAPITests {

    private int orderId;
    private Order createOrder;
    private String expectedShipDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).substring(0, 10);

    @BeforeClass
    public void setup() {
        // Configuração da URL base para a API
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    // POST Criação de ordem sucesso
    @Test(priority = 1)
    public void testGetStoreCreateOrder() {
        try {
            // Criador de objeto
            this.createOrder = new Order(997163, 10, 1, OrderStatus.placed, true);
            // Validador se a data corresponde com uma data valida
            this.expectedShipDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).substring(0, 10);
            Response response = given()
                    .contentType("application/json")
                    .body(createOrder)
                    .when()
                    .post("/store/order")
                    .then()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .body("petId", equalTo(createOrder.getPetId()))
                    .body("quantity", equalTo(createOrder.getQuantity()))
                    .body("shipDate", startsWith(expectedShipDate))
                    .body("status", equalTo(createOrder.getStatus().toString()))
                    .body("complete", equalTo(true))
                    .extract()
                    .response();

            this.orderId = response.path("id");

        } catch (Exception e) {
            System.err.println("Erro:" + e.getMessage());

        }

    }

    @Test(priority = 3)
    public void testGetStoreInventory() {
        try {
            int statusCode;
            // Teste GET: Verificando o inventário da loja
            Response response = given()
                    .when()
                    .get("/store/inventory")
                    .then()
                    .statusCode(200) 
                    .body("$", not(empty())) 
                    .body("available", greaterThanOrEqualTo(0))
                    .extract()
                    .response();
                
                statusCode = response.getStatusCode();

                if (statusCode == 200){

                }

        } catch (Exception e) {
            System.err.println("Erro:" + e.getMessage());
        }
    }

    // get order sucesso
    @Test(priority = 2, dependsOnMethods = "testGetStoreCreateOrder")
    public void testGetStoreOrderByid() {
        try {
            int statusCode;
            // Teste GET: Verificando o inventário da loja by id
            Response response = given()
                    .pathParam("orderId", this.orderId)
                    .when()
                    .get("store/order/{orderId}")
                    .then()
                    .extract()
                    .response();

            statusCode = response.getStatusCode();

            if (statusCode == 200) {
                System.out.println("Pedido encontrado 200 OK");
                response.then()
                        .body("id", equalTo(this.orderId))
                        .body("petId", equalTo(this.createOrder.getPetId()))
                        .body("quantity", equalTo(this.createOrder.getQuantity()))
                        .body("shipDate", startsWith(this.expectedShipDate))
                        .body("status", equalTo(this.createOrder.getStatus().toString()))
                        .body("complete", equalTo(true));

                this.orderId = response.path("id");

            } else if (statusCode == 404) {
                System.out.println("Pedido nao encontrado 404 Not Found");
            } else if (statusCode == 400) {
                System.out.println("Requisicao invalida 400 Bad Request");
            } else {
                System.out.println("Resposta inesperada" + statusCode);
            }

        } catch (Exception e) {
            System.err.println("Erro:" + e.getMessage());
        }
    }

    @Test(priority = 4, dependsOnMethods = "testGetStoreOrderByid")
    public void testDeleteStoreOrder() {
        try {
            int statusCode;
            int deleteOrderId = this.orderId;
            Response response = given()
                    .pathParam("orderId", deleteOrderId)
                    .when()
                    .delete("store/order/{orderId}")
                    .then()
                    .extract()
                    .response();

            statusCode = response.getStatusCode();

            if (statusCode == 200) {
                System.out.println("Pedido deletado 200 OK");

            } else if (statusCode == 404) {
                System.out.println("Pedido nao encontrado 404 Not Found");
            } else if (statusCode == 400) {
                System.out.println("Requisicao invalida 400 Bad Request");
            } else {
                System.out.println("Resposta inesperada" + statusCode);
            }

        } catch (Exception e) {
            System.err.println("Erro:" + e.getMessage());
        }
    }
}