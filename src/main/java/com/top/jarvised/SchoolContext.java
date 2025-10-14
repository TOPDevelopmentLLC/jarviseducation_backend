package com.top.jarvised;

public class SchoolContext {
    private static final ThreadLocal<String> CURRENT_SCHOOL = new ThreadLocal<>();

    public static void setSchool(String school) {
        CURRENT_SCHOOL.set(school);
    }

    public static String getSchool() {
        return CURRENT_SCHOOL.get();
    }

    public static void clear() {
        CURRENT_SCHOOL.remove();
    }
}

