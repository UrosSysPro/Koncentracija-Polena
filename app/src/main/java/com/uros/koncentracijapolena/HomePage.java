package com.uros.koncentracijapolena;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.uros.koncentracijapolena.tables.Alergen;
import com.uros.koncentracijapolena.tables.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class HomePage extends Page{
    private RequestQueue requestQueue;
    private MaterialSpinner locationsSpinner;
    private Location[] locations;
    private String[] alergenTypes;
    private Alergen[][] alergens;
    private LinearLayout datePicker;
    private EditText yearEdit,monthEdit,dayEdit;
    private TableLayout infoTable;
    private Button searchBtn;
    public HomePage(Context context){
        super(context);
        view.setBackgroundColor(Color.GREEN);
        locations=null;
        alergens=null;
        alergenTypes=null;
        view.setOrientation(LinearLayout.VERTICAL);
        //prepare ui
        setSpinner();
        setDatePicker();
        setSearchBtn();
        //prepare locations, alergens
        requestQueue=Volley.newRequestQueue(context);
        loadLocations();
        loadAlergenTypes();
    }
    private void setSpinner(){
        locationsSpinner=new MaterialSpinner(context);
        view.addView(locationsSpinner);
        locationsSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(locations==null)return;
                Location l=locations[position];
                MainActivity.debug.print(l.id+" "+l.name);
            }
        });
    }
    private void setDatePicker(){
        datePicker=new LinearLayout(context);
        datePicker.setOrientation(LinearLayout.HORIZONTAL);
        yearEdit=new EditText(context);
        monthEdit=new EditText(context);
        dayEdit=new EditText(context);
        yearEdit.setHint("YYYY");
        monthEdit.setHint("MM");
        dayEdit.setHint("DD");
        yearEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        monthEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        dayEdit.setInputType(InputType.TYPE_CLASS_NUMBER);

        yearEdit.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2));
        monthEdit.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        dayEdit.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));

        datePicker.addView(yearEdit);
        datePicker.addView(monthEdit);
        datePicker.addView(dayEdit);

        view.addView(datePicker);
    }
    private void setTables(){
        infoTable=new TableLayout(context);
        for(int i=0;i<alergenTypes.length;i++){
            for(int j=0;j<alergens[i].length;j++){
                TableRow tableRow=new TableRow(context);
                TextView t1=new TextView(context);
                TextView t2=new TextView(context);
                t1.setText(alergens[i][j].localName);
                t2.setText("0");
                tableRow.addView(t1);
                tableRow.addView(t2);
                TableRow.LayoutParams params=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                t1.setLayoutParams(params);
                t2.setLayoutParams(params);
                infoTable.addView(tableRow);
            }
        }
        infoTable.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.addView(infoTable);
    }
    private void setSearchBtn(){
        searchBtn=new Button(context);
        searchBtn.setText("Search");
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchBtn.setLayoutParams(params);
        view.addView(searchBtn);
    }

    private void loadAlergenTypes(){
        String url = "http://polen.sepa.gov.rs/api/opendata/allergen-types/";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                      upisati u niz tipova i napraviti prvi niz
//                        MainActivity.debug.print(response.toString());
                        try {
                            alergenTypes=new String[response.length()];
                            alergens=new Alergen[response.length()][];
                            for(int i=0;i<response.length();i++){
                                JSONObject o=(JSONObject)response.get(i);
                                alergenTypes[i]=o.getString("name");
                            }
                            loadAlergens();
                        }catch (Exception e){
                            MainActivity.debug.print("greska pri citanju tipova alergena");
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
    private void loadAlergens(){
        String url = "http://polen.sepa.gov.rs/api/opendata/allergens/";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                      upisati u matrici alergena
//                        MainActivity.debug.print(response.toString());
                        try {
                            LinkedList<Alergen>[] lista=new LinkedList[alergenTypes.length];
                            for(int i=0;i<alergenTypes.length;i++){
                                lista[i]=new LinkedList<>();
                            }
                            for(int i=0;i<response.length();i++){
                                JSONObject o=(JSONObject)response.get(i);
                                int type=o.getInt("type");
                                lista[type-1].push(new Alergen(o.getInt("id"),o.getString("name"),o.getString("localized_name")));
                            }
                            for(int i=0;i<alergens.length;i++){
                                alergens[i]=new Alergen[lista[i].size()];
                                for(int j=0;j<alergens[i].length;j++){
                                    alergens[i][j]=lista[i].get(j);
                                }
                            }
                            printAlergens();
                            setTables();
                        }catch (Exception e){
                            MainActivity.debug.print(e.getMessage());
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
    private void loadLocations(){
        String url = "http://polen.sepa.gov.rs/api/opendata/locations/";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject o=(JSONObject) response.get(i);
//                                int id=o.getInt("id");
//                                String name=o.getString("name");
//                                String description=o.getString("description");
//                                MainActivity.debug.print(id+" "+name+" "+description);
//                            }
//                        }catch (Exception e){
//                            MainActivity.debug.print("greska pri citanju json");
//                        }
                        addLocationsToSpinner(response);
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
        try{
            locations=new Location[array.length()];
            ArrayList<String> list=new ArrayList<>();
            for(int i=0;i<array.length();i++){
                JSONObject o=(JSONObject) array.get(i);
                String name=o.getString("name");
                list.add(name);
                locations[i]=new Location(o.getInt("id"),o.getString("name"),o.getString("description"));
            }
            locationsSpinner.setItems(list);
        }catch (Exception e){
            MainActivity.debug.print("greska pri ubacivanju u spinner");
        }
    }
    private void printAlergens(){
        for(int i=0;i<alergenTypes.length;i++){
            MainActivity.debug.print(alergenTypes[i]);
            for(int j=0;j<alergens[i].length;j++){
                MainActivity.debug.print(alergens[i][j].id+" "+alergens[i][j].name+" "+alergens[i][j].localName);
            }
        }
    }
}
