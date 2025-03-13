package com.automacao.petstore.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// MODEL
public class OrderCreate {

    private int petId;
    private int quantity;
    private String shipDate;
    private OrderStatus status;
    private boolean complete;

    // Construtor
    public OrderCreate(int petId, int quantity, OrderStatus status, boolean complete) {
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
        this.status = status;
        this.complete = complete;
    }

    // Getter PETID
    public int getPetId() {
        return petId;
    }

    // Setter PETID
    public void setPetId(int petId) {
        this.petId = petId;
    }

    // Getter QUANTITY
    public int getQuantity() {
        return quantity;
    }

    // Setter QUANTITY
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter SHIPDATE
    public String getShipDate() {
        return shipDate;
    }

    // Setter SHIPDATE
    public void setShipdate(String shipDate) {
        this.shipDate = shipDate;
    }

    // Getter STATUS
    public OrderStatus getStatus() {
        return status;
    }

    // Setter STATUS
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // Getter COMPLETE
    public boolean getComplete() {
        return complete;
    }

    // Setter COMPLETE
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
