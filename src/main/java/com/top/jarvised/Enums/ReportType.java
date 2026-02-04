package com.top.jarvised.Enums;

public enum ReportType {
    ABC ("ABC"),
    Attendance ("Attendance"),
    Behavior ("Behavior"),
    Conflict ("Conflict"),
    Expelled ("Expelled"),
    Mood ("Mood"),
    Secluded ("Secluded"),
    SIP ("SIP"),
    None ("None");

    private final String valueAsString;
    private ReportType(String value) {
        valueAsString = value;
    }

    @Override
    public String toString() {
        return this.valueAsString;
    }

    public ReportType getReportTypeFromString(String value) {
        switch (value) {
            case "ABC":
                return ReportType.ABC;
            case "Attendance":
                return ReportType.Attendance;
            case "Behavior":
                return ReportType.Behavior;
            case "Conflict":
                return ReportType.Conflict;
            case "Expelled":
                return ReportType.Expelled;
            case "Mood":
                return ReportType.Mood;
            case "Secluded":
                return ReportType.Secluded;
            case "SIP":
                return ReportType.SIP;
            default:
                return ReportType.None;
        }
    }
}
