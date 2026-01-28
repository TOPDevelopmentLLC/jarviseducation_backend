package com.top.jarvised.DTOs;

import java.time.LocalDate;

public class AddHolidayRequest {
    private String name;
    private LocalDate date;
    private String description;

    public AddHolidayRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
