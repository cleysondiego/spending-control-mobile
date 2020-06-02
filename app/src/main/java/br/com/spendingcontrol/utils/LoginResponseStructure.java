package br.com.spendingcontrol.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponseStructure {
    private UserConfig user;

    private String token;

    public UserConfig getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public LoginResponseStructure fromJson(JSONObject jsonObject) {
        try {
            user = new UserConfig().fromJson(jsonObject.getJSONObject("user"));
            token = jsonObject.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public static class UserConfig {
        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        UserConfig fromJson(JSONObject jsonObject) {
            try {
                this.name = jsonObject.getString("name");
                this.email = jsonObject.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }
    }
}
