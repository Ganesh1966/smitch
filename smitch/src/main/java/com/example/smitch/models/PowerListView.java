package com.example.smitch.models;

import com.example.demo.db.jooqs.enums.ApplicationType;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PowerListView {
    private UUID id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "PowerListView{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", unitConsumed='" + unitConsumed + '\'' +
                ", applicaionType=" + applicaionType +
                '}';
    }
}
