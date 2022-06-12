package com.example.health_iot_app.models;

public class NestedHistoryItemModel {
    private String value;
    private String unit;
    private String qualify;

    public NestedHistoryItemModel(String value, String unit, String qualify) {
        this.value = value;
        this.unit = unit;
        this.qualify = qualify;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQualify() {
        return qualify;
    }

    public void setQualify(String qualify) {
        this.qualify = qualify;
    }
}
