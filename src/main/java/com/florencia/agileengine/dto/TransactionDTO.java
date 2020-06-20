package com.florencia.agileengine.dto;

import java.util.Objects;

public class TransactionDTO {
    private String type;
    private double amount;

    public TransactionDTO() {
    }

    public TransactionDTO(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO that = (TransactionDTO) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
