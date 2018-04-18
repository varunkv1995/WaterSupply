package com.pentateuch.watersupply.model;

public class Status {
    private String dboyId;
    private String orderDate;
    private String value;

    public Status() {
    }

    public Status(String value) {
        this.value = value;
    }

    public String getDboyId() {
        return dboyId;
    }

    public void setDboyId(String dboyId) {
        this.dboyId = dboyId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
