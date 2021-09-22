package com.uros.koncentracijapolena;

import android.content.Context;
import android.graphics.Color;
import android.widget.Spinner;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends Page{
    private RequestQueue requestQueue;
    private Spinner locationsSpinner;
    public HomePage(Context context){
        super(context);
        view.setBackgroundColor(Color.GREEN);
        locationsSpinner=new Spinner(context);
        loadLocations();
    }
    private void loadLocations(){
        requestQueue=Volley.newRequestQueue(context);
        String url = "http://polen.sepa.gov.rs/api/opendata/locations/";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject o=(JSONObject) response.get(i);
                                int id=o.getInt("id");
                                String name=o.getString("name");
                                String description=o.getString("description");
                                MainActivity.debug.print(id+" "+name+" "+description);
                            }
                        }catch (Exception e){
                            MainActivity.debug.print("greska pri citanju json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MainActivity.debug.print(error.getMessage());
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("accept","application/json");
                map.put("Content-Type", "application/json; charset=UTF-8");
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    private void addLocationsToSpinner(JSONArray array){

    }
}
