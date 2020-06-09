package br.com.spendingcontrol.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetSpendingsResponseStructure {
    private List<Spendings> spendings = new ArrayList<>();
    private Integer balance;

    public List<Spendings> getSpendings() {
        return spendings;
    }

    public Integer getBalance() {
        return balance;
    }

    public GetSpendingsResponseStructure fromJson(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("spendings");

            for (int i = 0; i< jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                spendings.add(new Spendings().fromJson(object));
            }

            balance = jsonObject.getInt("saldo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public static class Spendings implements Serializable {
        private String id;
        private String title;
        private String description;
        private Double value;
        private String type;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Double getValue() {
            return value;
        }

        public String getType() {
            return type;
        }

        public Spendings fromJson(JSONObject jsonObject) {
            try {
                this.id = jsonObject.getString("_id");
                this.title = jsonObject.getString("title");
                this.description = jsonObject.getString("description");
                this.value = jsonObject.getDouble("value");
                this.type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }
    }
}