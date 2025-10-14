package com.top.jarvised.Enums;

public enum AccountType {
    
    Student ("Student"),
    Teacher ("Teacher"),
    Administrator ("Administrator"),
    Parent ("Parent"),
    Master ("Master"),
    None ("None");

    private final String valueAsString;
    private AccountType(String value) {
        valueAsString = value;
    }

    @Override
    public String toString() {
        return this.valueAsString;
    }

    public AccountType getAccountTypeFromString(String value) {
        switch (value) {
            case "Student":
                return AccountType.Student;
            case "Teacher":
                return AccountType.Teacher;
            case "Administrator":
                return AccountType.Administrator;
            case "Master":
                return AccountType.Master;
            case "Parent":
                return AccountType.Parent;
            default:
                return AccountType.None;
        }
    }

}
