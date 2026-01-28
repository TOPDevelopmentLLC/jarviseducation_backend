package com.top.jarvised.Enums;

public enum TermType {

    Semester("Semester"),
    Trimester("Trimester"),
    Quarter("Quarter"),
    None("None");

    private final String valueAsString;

    private TermType(String value) {
        valueAsString = value;
    }

    @Override
    public String toString() {
        return this.valueAsString;
    }

    public static TermType getFromString(String value) {
        switch (value) {
            case "Semester":
                return TermType.Semester;
            case "Trimester":
                return TermType.Trimester;
            case "Quarter":
                return TermType.Quarter;
            default:
                return TermType.None;
        }
    }

    public int getExpectedTermCount() {
        switch (this) {
            case Semester:
                return 2;
            case Trimester:
                return 3;
            case Quarter:
                return 4;
            default:
                return 0;
        }
    }
}
