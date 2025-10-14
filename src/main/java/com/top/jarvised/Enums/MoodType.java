package com.top.jarvised.Enums;

public enum MoodType {
    Green ("Green - All is good."), 
    Blue ("Blue - Feeling a little bit off."), 
    Yellow ("Yellow - Can't sit still."), 
    Red ("Red - Something is bothering me."),
    None ("None");

    private final String valueAsString;
    private MoodType(String value) {
        valueAsString = value;
    }

    @Override
    public String toString() {
        return this.valueAsString;
    }

    public MoodType getMoodTypeFromString(String value) {
        switch (value) {
            case "Green - All is good.":
                return MoodType.Green;
            case "Blue - Feeling a little bit off.":
                return MoodType.Blue;
            case "Yellow - Can't sit still.":
                return MoodType.Yellow;
            case "Red - Something is bothering me.":
                return MoodType.Red;
            default:
                return MoodType.None;
        }
    }
}
