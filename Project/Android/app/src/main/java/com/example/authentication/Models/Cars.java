package com.example.authentication.Models;

import java.io.Serializable;

public class Cars implements Serializable {
    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    private String cars;
}
