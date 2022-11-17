package com.example.smitch.models;

import com.example.demo.db.jooqs.enums.ApplicationType;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Power {
    private UUID id;
    private UUID userId;
    private OffsetDateTime fromTime;
    private OffsetDateTime toTime;
    private String unitConsumed;
    private ApplicationType applicaionType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OffsetDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(OffsetDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public OffsetDateTime getToTime() {
        return toTime;
    }

    public void setToTime(OffsetDateTime toTime) {
        this.toTime = toTime;
    }

    public String getUnitConsumed() {
        return unitConsumed;
    }

    public void setUnitConsumed(String unitConsumed) {
        this.unitConsumed = unitConsumed;
    }

    public ApplicationType getApplicaionType() {
        return applicaionType;
    }

    public void setApplicaionType(ApplicationType applicaionType) {
        this.applicaionType = applicaionType;
    }



    @Override
    public String toString() {
        return "Power{" +
                "id=" + id +
                ", userId=" + userId +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", unitConsumed='" + unitConsumed + '\'' +
                ", applicaionType=" + applicaionType +
                '}';
    }
}
