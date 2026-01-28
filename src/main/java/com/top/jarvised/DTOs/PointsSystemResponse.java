package com.top.jarvised.DTOs;

import com.top.jarvised.Entities.PointsSystem;

public class PointsSystemResponse {
    private Integer attendanceDeduction;
    private Integer behaviorDeduction;
    private Integer conflictDeduction;
    private Integer expelledDeduction;
    private Integer moodDeduction;
    private Integer secludedDeduction;
    private Integer sipDeduction;

    public PointsSystemResponse() {}

    public PointsSystemResponse(PointsSystem pointsSystem) {
        this.attendanceDeduction = pointsSystem.getAttendanceDeduction();
        this.behaviorDeduction = pointsSystem.getBehaviorDeduction();
        this.conflictDeduction = pointsSystem.getConflictDeduction();
        this.expelledDeduction = pointsSystem.getExpelledDeduction();
        this.moodDeduction = pointsSystem.getMoodDeduction();
        this.secludedDeduction = pointsSystem.getSecludedDeduction();
        this.sipDeduction = pointsSystem.getSipDeduction();
    }

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
}
