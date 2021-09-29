package com.uros.koncentracijapolena;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.uros.koncentracijapolena.tables.Allergen;
import com.uros.koncentracijapolena.tables.AllergenType;
import com.uros.koncentracijapolena.tables.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class HomePage extends Page{
    private RequestQueue requestQueue;
    private MaterialSpinner locationsSpinner;
    private Location[] locations;
    private AllergenType[] allergenTypes;
    private Allergen[] allergens;
    private LinearLayout datePicker;
    private EditText yearEdit,monthEdit,dayEdit;
    private TableLayout infoTable;
    private Button searchBtn;
    private Button reloadBtn;
    private TextView[] concentrationTextViews;
    private int selectedLocation;
    public HomePage(Context context){
        super(context);
        reload();
    }
//    setup za ui
    private void setSpinner(){
        locationsSpinner=new MaterialSpinner(context);
        view.addView(locationsSpinner);
        locationsSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(locations==null)return;
                Location l=locations[position];
                //MainActivity.debug.print(l.id+" "+l.name);
                selectedLocation=l.id;
            }
        });
    }
    private void setDatePicker(){
        datePicker=new LinearLayout(context);
        datePicker.setOrientation(LinearLayout.HORIZONTAL);
        yearEdit=new EditText(context);
        monthEdit=new EditText(context);
        dayEdit=new EditText(context);
        yearEdit.setTextColor(Color.WHITE);
        monthEdit.setTextColor(Color.WHITE);
        dayEdit.setTextColor(Color.WHITE);
        yearEdit.setHintTextColor(Color.WHITE);
        monthEdit.setHintTextColor(Color.WHITE);
        dayEdit.setHintTextColor(Color.WHITE);
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

        yearEdit.setText("2017");
        monthEdit.setText("5");
        dayEdit.setText("5");

        view.addView(datePicker);
    }
    private void setTables(){
        infoTable=new TableLayout(context);
        int k=0;
        for(int i=0;i<allergenTypes.length;i++){
            addTableRow(allergenTypes[i].name,"concentration");
            for(int j=0;j<allergens.length;j++){
                if(allergens[j].typeId==allergenTypes[i].id){
                    concentrationTextViews[k]=addTableRow(allergens[j].name,"0");
                    allergens[j].textViewId=k;
                    k++;
                }
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
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInput();
            }
        });
        view.addView(searchBtn);
    }
    private void setReloadBtn(){
        reloadBtn=new Button(context);
        reloadBtn.setText("Reload");
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reloadBtn.setLayoutParams(params);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        view.addView(reloadBtn);
    }
    private TextView addTableRow(String text1,String text2){
        TableRow tableRow=new TableRow(context);
        TextView t1=new TextView(context);
        TextView t2=new TextView(context);
        t1.setTextColor(Color.WHITE);
        t2.setTextColor(Color.WHITE);
        t1.setText(text1);
        t2.setText(text2);
        tableRow.addView(t1);
        tableRow.addView(t2);
        TableRow.LayoutParams params=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        t1.setLayoutParams(params);
        t2.setLayoutParams(params);
        infoTable.addView(tableRow);
        return t2;
    }

    //pretraga polena
    public void checkUserInput(){
        if(selectedLocation==-1){
            MainActivity.debug.print("nije selektovan grad");
            return;
        }
        int locationId=selectedLocation;
        int year,month,day;
        try{
            year=Integer.parseInt(yearEdit.getText().toString());
            month=Integer.parseInt(monthEdit.getText().toString());
            day=Integer.parseInt(dayEdit.getText().toString());
            if(year>2100||year<1970||month>12||month<1||day<1||day>31){
                MainActivity.debug.print("pogresan format");
                return;
            }
        }catch (Exception e){
            MainActivity.debug.print("nisu unet pravilan datum");
            return;
        }
        Date date=new Date(year,month,day);
        date.setTime(date.getTime()+6*24*60*60*1000);
//        MainActivity.debug.print(date.getYear()+" "+date.getMonth()+" "+date.getDate());
        getPollenIdAtLocationAndDate(locationId,year,month,day);
        String start=year+"-"+month+"-"+day;
        String end=date.getYear()+"-"+date.getMonth()+"-"+date.getDate();
        getPollenIdsAtLocationAndInterval(locationId,start,end);
    }
    private void getPollenIdAtLocationAndDate(int locationId,int year,int month,int day){
        String url = "http://polen.sepa.gov.rs/api/opendata/pollens/";
        url+="?location=";
        url+=locationId;
        url=url+"&date="+year+"-"+month+"-"+day;
        for(int i=0;i<allergens.length;i++){
            enterAllergenValueIntoTable(0,i);
        }
        CustomObjectRequest request=new CustomObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.getInt("count")==1){
                        JSONArray array=(JSONArray) response.get("results");
                        JSONObject o=(JSONObject) array.get(0);
                        int id=o.getInt("id");
                        getConcentrationAtPollenId(id);
                    }else{
                        MainActivity.debug.print("nema nijednog polena tog dana");
                    }
                }catch (Exception e){
                    MainActivity.debug.print(e.getMessage());
                    MainActivity.debug.print("Error parsing json getPollenAtLocationAndDate");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error getPollenAtLocationAndDate");
            }
        });
        requestQueue.add(request);
    }
    private void getConcentrationAtPollenId(int pollenId){
        String url = "http://polen.sepa.gov.rs/api/opendata/concentrations/";
        url=url+"?pollen="+pollenId;
        CustomObjectRequest request=new CustomObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    int len=response.getInt("count");
                    JSONArray array=response.getJSONArray("results");
                    for(int i=0;i<len;i++){
                        int value=array.getJSONObject(i).getInt("value");
                        int allergenId=array.getJSONObject(i).getInt("allergen");
                        enterAllergenValueIntoTable(value,allergenId);
                    }
                }catch (Exception e){
                    MainActivity.debug.print(e.getMessage());
                    MainActivity.debug.print("Error parsing json getConcentrationsAtId");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error getConcentrationAtPollenId");
            }
        });
        requestQueue.add(request);
    }
    private void enterAllergenValueIntoTable(int value,int allergenId){
        for(int i=0;i<allergens.length;i++){
            if(allergens[i].id==allergenId){
                concentrationTextViews[allergens[i].textViewId].setText(value+"");
                break;
            }
        }
    }
    //pretraga za interval
    private void getPollenIdsAtLocationAndInterval(int locationId,String start,String end){
        String url="http://polen.sepa.gov.rs/api/opendata/pollens/";
        url+="?location="+locationId;
        url+="&date_after="+start;
        url+="&date_before="+end;

        CustomObjectRequest request=new CustomObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                MainActivity.debug.print(response.toString());
                try{
                    int len=response.getInt("count");
                    JSONArray array=response.getJSONArray("results");
                    int[] ids=new int[len];
                    for(int i=0;i<len;i++){
                        ids[i]=array.getJSONObject(i).getInt("id");
                    }
                    getConcentrationsAtPollenIds(ids);
                }catch (Exception e){
                    MainActivity.debug.print("Error parsing json getPollenIdAtInterval");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error getpollenIdsAtInterval");
            }
        });
        requestQueue.add(request);
    }
    private void getConcentrationsAtPollenIds(int[] ids){
        String url="http://polen.sepa.gov.rs/api/opendata/concentrations/";
        for(int i=0;i<ids.length;i++){
            if(i==0)url+="?pollen_ids="+ids[i];
            else url+="&pollen_ids="+ids[i];
        }
        CustomObjectRequest request=new CustomObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MainActivity.debug.print(response.toString());
                //nacrtati grafik sa
                try {
                    JSONArray array = response.getJSONArray("results");
                }catch (Exception e){
                    MainActivity.debug.print("Error parsing json getConcentrationsAtPollenIds");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error getConcentrationsAtPollenIds");
            }
        });
        requestQueue.add(request);
    }

    private void reload(){
        view.removeAllViews();
//        view.setBackgroundColor(Color.GREEN);
        locations=null;
        allergens =null;
        allergenTypes =null;
        selectedLocation=-1;
        view.setOrientation(LinearLayout.VERTICAL);
        //prepare ui
        setSpinner();
        setDatePicker();
        setSearchBtn();
        setReloadBtn();
        //prepare locations, alergens
        requestQueue=Volley.newRequestQueue(context);
        loadLocations();
        loadAllergenTypes();
    }

    //setup
    private void loadAllergenTypes(){
        String url = "http://polen.sepa.gov.rs/api/opendata/allergen-types/";
        CustomArrayRequest request=new CustomArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                upisati u niz tipova i napraviti prvi niz
//                 MainActivity.debug.print(response.toString());
                 try {
                     allergenTypes =new AllergenType[response.length()];
                     for(int i=0;i<response.length();i++){
                         JSONObject o=(JSONObject)response.get(i);
                         allergenTypes[i]=new AllergenType(o.getInt("id"),o.getString("name"));
                     }
                     loadAllergens();
                 }catch (Exception e){
                     MainActivity.debug.print("greska pri citanju tipova alergena");
                 }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error loading alergenTypes");
            }
        });
        requestQueue.add(request);
    }
    private void loadAllergens(){
        String url = "http://polen.sepa.gov.rs/api/opendata/allergens/";

        CustomArrayRequest request=new CustomArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    concentrationTextViews=new TextView[response.length()];
                    allergens=new Allergen[response.length()];
                    for(int i=0;i<response.length();i++){
                        JSONObject o=(JSONObject) response.get(i);
                        allergens[i]=new Allergen(o.getInt("id"),o.getInt("type"),o.getString("localized_name"),o.getString("name"));
                    }
                    setTables();
                }catch (Exception e){
                    MainActivity.debug.print("Error parsing json loadAllergens");
                    MainActivity.debug.print(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error LoadAllergens");
            }
        });
        requestQueue.add(request);
    }
    private void loadLocations(){
        String url = "http://polen.sepa.gov.rs/api/opendata/locations/";
        CustomArrayRequest request=new CustomArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                addLocationsToSpinner(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error LoadLocations");
            }
        });
        requestQueue.add(request);
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
}
