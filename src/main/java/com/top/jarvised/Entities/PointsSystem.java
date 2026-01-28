package com.top.jarvised.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "points_system")
public class PointsSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userAccountId;

    @Column(nullable = false)
    private Integer attendanceDeduction = 0;

    @Column(nullable = false)
    private Integer behaviorDeduction = 0;

    @Column(nullable = false)
    private Integer conflictDeduction = 0;

    @Column(nullable = false)
    private Integer expelledDeduction = 0;

    @Column(nullable = false)
    private Integer moodDeduction = 0;

    @Column(nullable = false)
    private Integer secludedDeduction = 0;

    @Column(nullable = false)
    private Integer sipDeduction = 0;

    public PointsSystem() {}

    public PointsSystem(Long userAccountId) {
        this.userAccountId = userAccountId;
        this.attendanceDeduction = 0;
        this.behaviorDeduction = 0;
        this.conflictDeduction = 0;
        this.expelledDeduction = 0;
        this.moodDeduction = 0;
        this.secludedDeduction = 0;
        this.sipDeduction = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
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
