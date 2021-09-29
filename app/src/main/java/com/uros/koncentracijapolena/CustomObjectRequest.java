package com.uros.koncentracijapolena;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomObjectRequest extends JsonObjectRequest {
    public CustomObjectRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(Method.GET,url,null,listener,errorListener);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("accept","application/json");
        map.put("Content-Type", "application/json; charset=UTF-8");
        return map;
    }
    @Override
    public String getBodyContentType() {
        return "application/json; charset=UTF-8";
    }
}
