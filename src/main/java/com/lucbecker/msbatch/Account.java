package com.lucbecker.msbatch;

import javax.validation.constraints.NotNull;

public class Account {

    @NotNull
    private String id;
    private String customer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
