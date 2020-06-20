package com.florencia.agileengine.dto;

import com.florencia.agileengine.domain.Transaction;

import java.util.Date;
import java.util.Objects;

public class TransactionHistoryDTO {

    private String id;
    private String type;
    private Double amount;
    private Date effectiveDate;

    public TransactionHistoryDTO() {
    }

    public TransactionHistoryDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.effectiveDate = transaction.getEffectiveDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionHistoryDTO that = (TransactionHistoryDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(effectiveDate, that.effectiveDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, amount, effectiveDate);
    }
}
