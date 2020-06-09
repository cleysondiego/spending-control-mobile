package br.com.spendingcontrol.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class AddNewSpendRequest {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("value")
    private Double value;

    @SerializedName("type")
    private String type;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
