package com.example.onlineclinic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class JsonParser {
    private String json;
    private JSONObject jsonObject;
    private LinkedHashMap<String, String> map;
    private int validate = 0;
    private int amount = 0;

    public LinkedHashMap<String, String> parseDoctors(String s) throws Exception {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (validate == 1 && amount > 0) {
            String[] temp = json.split("(?=\"0_id)");
            json = "{" + temp[1];
            jsonObject = new JSONObject(json);

            for (int i = 0; i < amount; i++) {
                for (int j = 0; j < 6; j++) {
                    String key = jsonObject.names().getString(i * 6 + j);
                    String value = jsonObject.get(key).toString();

                    map.put(key, value);
                }
            }
        }

        return map;
    }

    public LinkedHashMap<String, String> parseServices(String s) throws Exception {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (validate == 1 && amount > 0) {
            String[] temp = json.split("(?=\"0_id)");
            json = "{" + temp[1];
            jsonObject = new JSONObject(json);

            for (int i = 0; i < amount; i++) {
                for (int j = 0; j < 6; j++) {
                    String key = jsonObject.names().getString(i * 6 + j);
                    String value = jsonObject.get(key).toString();

                    map.put(key, value);
                }
            }
        }

        return map;
    }

    public int getQueryAmount(String s) throws Exception {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return amount;
    }

    public LinkedHashMap<String, String> parseLogin(String s) throws Exception {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (validate > 0) {
            jsonObject = new JSONObject(json);

            String key = "id";
            String value = jsonObject.getString("id");

            map.put(key, value);

            key = "accountType";
            value = jsonObject.getString("accountType");
            map.put(key, value);

            key = "clientName";
            value = jsonObject.getString("clientName");

            map.put(key, value);
        }

        return map;
    }

    public LinkedHashMap<String, String> parseSignUp(String s) {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            map.put("success", String.valueOf(validate));

            if (validate > 0) {
                jsonObject = new JSONObject(json);

                String key = "id";
                String value = jsonObject.getString("id");

                map.put(key, value);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;
    }

    public LinkedHashMap<String, String> parseJson(String s) throws Exception {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (validate == 1 && amount > 0) {
            String[] temp = json.split("(?=\"id_0)");
            json = "{" + temp[1];
            jsonObject = new JSONObject(json);

            for (int i = 0; i < amount; i++) {
                for (int j = 0; j < 4; j++) {
                    String key = jsonObject.names().getString(i * 4 + j);
                    String value = jsonObject.get(key).toString();

                    map.put(key, value);
                }
            }
        }

        return map;
    }


    public LinkedHashMap<String, String> parseWorkers(String s) throws Exception {
        json = s;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (validate != 0 && amount != 0) {
            jsonObject = new JSONObject(json);

            for (int i = 0; i < amount; i++) {
                String key = i + "_idWorker";
                String value = jsonObject.getString(key);

                map.put(key, value);

                key = i + "_Name";
                value = jsonObject.getString(key);

                map.put(key, value);

                key = i + "_Surname";
                value = jsonObject.getString(key);

                map.put(key, value);
            }
        }

        return map;
    }


    public LinkedHashMap<String, String> parseHours(String h) throws Exception {
        json = h;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            if (validate > 0)
                amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < amount; i++) {
            String key = i + "_hour";
            String value = jsonObject.getString(key);

            map.put(key, value);
        }
        return map;
    }

    public LinkedHashMap<String, String> parseUserProfile(String p) throws Exception {
        json = p;
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String key = "surname";
        String value = jsonObject.getString(key);

        map.put(key, value);

        key = "name";
        value = jsonObject.getString(key);

        map.put(key, value);

        key = "name2";
        value = jsonObject.getString(key);

        map.put(key, value);


        key = "email";
        value = jsonObject.getString(key);

        map.put(key, value);

        return map;
    }

    public LinkedHashMap<String, String> parseOrders(String o) throws Exception {
        json = o;

        System.out.println("JSON: " + json);
        map = new LinkedHashMap<>();

        try {
            jsonObject = new JSONObject(json);
            validate = jsonObject.getInt("success");
            amount = jsonObject.getInt("query_amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < amount; i++) {
            String key = "nameService_" + i;
            String value = jsonObject.getString(key);

            map.put(key, value);

            key = "name_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "surname_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "dateVisit_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "hourVisit_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "nameOfCompany_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "city_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "street_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);

            key = "payInAdvance_" + i;
            value = jsonObject.getString(key);

            map.put(key, value);
        }

        return map;
    }
}