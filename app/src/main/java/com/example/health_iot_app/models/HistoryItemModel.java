package com.example.health_iot_app.models;

import java.util.List;

public class HistoryItemModel {
    private List<NestedHistoryItemModel> nestedList;
    private String parameterText;
    private boolean isExpandable;
    private int imageId;

    public HistoryItemModel(List<NestedHistoryItemModel> nestedList, String parameterText, int imageId) {
        this.nestedList = nestedList;
        this.parameterText = parameterText;
        this.imageId=imageId;
        isExpandable = false;
    }

    public List<NestedHistoryItemModel> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<NestedHistoryItemModel> nestedList) {
        this.nestedList = nestedList;
    }

    public String getParameterText() {
        return parameterText;
    }

    public void setParameterText(String parameterText) {
        this.parameterText = parameterText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
