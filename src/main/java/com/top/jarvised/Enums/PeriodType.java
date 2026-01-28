package com.top.jarvised.Enums;

public enum PeriodType {

    Class("Class"),
    Lunch("Lunch"),
    Homeroom("Homeroom"),
    Passing("Passing Period"),
    Assembly("Assembly"),
    Other("Other");

    private final String valueAsString;

    private PeriodType(String value) {
        valueAsString = value;
    }

    @Override
    public String toString() {
        return this.valueAsString;
    }

    public static PeriodType getFromString(String value) {
        switch (value) {
            case "Class":
                return PeriodType.Class;
            case "Lunch":
                return PeriodType.Lunch;
            case "Homeroom":
                return PeriodType.Homeroom;
            case "Passing Period":
                return PeriodType.Passing;
            case "Assembly":
                return PeriodType.Assembly;
            default:
                return PeriodType.Other;
        }
    }
}
