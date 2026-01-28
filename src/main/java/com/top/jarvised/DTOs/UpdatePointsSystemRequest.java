package com.top.jarvised.DTOs;

public class UpdatePointsSystemRequest {
    private Integer attendanceDeduction;
    private Integer behaviorDeduction;
    private Integer conflictDeduction;
    private Integer expelledDeduction;
    private Integer moodDeduction;
    private Integer secludedDeduction;
    private Integer sipDeduction;
    private Integer dailyIncrease;

    public UpdatePointsSystemRequest() {}

    public Integer getAttendanceDeduction() {
        return attendanceDeduction;
    }

    public void setAttendanceDeduction(Integer attendanceDeduction) {
        this.attendanceDeduction = attendanceDeduction;
    }

    public Integer getBehaviorDeduction() {
        return behaviorDeduction;
    }

    public void setBehaviorDeduction(Integer behaviorDeduction) {
        this.behaviorDeduction = behaviorDeduction;
    }

    public Integer getConflictDeduction() {
        return conflictDeduction;
    }

    public void setConflictDeduction(Integer conflictDeduction) {
        this.conflictDeduction = conflictDeduction;
    }

    public Integer getExpelledDeduction() {
        return expelledDeduction;
    }

    public void setExpelledDeduction(Integer expelledDeduction) {
        this.expelledDeduction = expelledDeduction;
    }

    public Integer getMoodDeduction() {
        return moodDeduction;
    }

    public void setMoodDeduction(Integer moodDeduction) {
        this.moodDeduction = moodDeduction;
    }

    public Integer getSecludedDeduction() {
        return secludedDeduction;
    }

    public void setSecludedDeduction(Integer secludedDeduction) {
        this.secludedDeduction = secludedDeduction;
    }

    public Integer getSipDeduction() {
        return sipDeduction;
    }

    public void setSipDeduction(Integer sipDeduction) {
        this.sipDeduction = sipDeduction;
    }

    public Integer getDailyIncrease() {
        return dailyIncrease;
    }

    public void setDailyIncrease(Integer dailyIncrease) {
        this.dailyIncrease = dailyIncrease;
    }
}
