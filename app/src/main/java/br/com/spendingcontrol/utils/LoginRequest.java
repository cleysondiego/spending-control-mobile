package br.com.spendingcontrol.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEventString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
