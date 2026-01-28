package com.top.jarvised.Enums;

public enum BreakType {

    WinterBreak("Winter Break"),
    SpringBreak("Spring Break"),
    SummerBreak("Summer Break"),
    FallBreak("Fall Break"),
    ThanksgivingBreak("Thanksgiving Break"),
    Other("Other");

    private final String valueAsString;

    private BreakType(String value) {
        valueAsString = value;
    }

    @Override
    public String toString() {
        return this.valueAsString;
    }

    public static BreakType getFromString(String value) {
        switch (value) {
            case "Winter Break":
                return BreakType.WinterBreak;
            case "Spring Break":
                return BreakType.SpringBreak;
            case "Summer Break":
                return BreakType.SummerBreak;
            case "Fall Break":
                return BreakType.FallBreak;
            case "Thanksgiving Break":
                return BreakType.ThanksgivingBreak;
            default:
                return BreakType.Other;
        }
    }
}
