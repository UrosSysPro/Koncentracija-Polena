package com.uros.koncentracijapolena;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.cardview.widget.CardView;
import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
    private TextView[] changeTextViews;
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
        CardView cardView=new CardView(context);

        infoTable=new TableLayout(context);
        int k=0;
        for(int i=0;i<allergenTypes.length;i++){
            addTableRow(allergenTypes[i].name,"concentration","change",-1);
            for(int j=0;j<allergens.length;j++){
                if(allergens[j].typeId==allergenTypes[i].id){
                    addTableRow(allergens[j].name,"0","no data",k);
                    allergens[j].textViewId=k;
                    k++;
                }
            }
        }
        LinearLayout.LayoutParams paramsCard=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams paramsTable=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsCard.topMargin=30;
        cardView.setLayoutParams(paramsCard);
        infoTable.setLayoutParams(paramsTable);

        cardView.setCardBackgroundColor(Color.DKGRAY);
        cardView.setRadius(10);
        cardView.setPadding(10,10,10,10);

        infoTable.setPadding(0,25,0,25);

        cardView.addView(infoTable);
        view.addView(cardView);
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
    private void showReloadSnackBar(){
        Snackbar snackbar=Snackbar.make(view,"Check your connection", BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction("reload", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        snackbar.show();
    }
    private void showSearchSnackBar(){
        Snackbar snackbar=Snackbar.make(view,"Check your connection and try again", BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction("search", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInput();
            }
        });
        snackbar.show();
    }
    private void showMessageSnackBar(String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }
    private void addTableRow(String text1,String text2,String text3,int k){
        TableRow tableRow=new TableRow(context);
        TextView t1=new TextView(context);
        TextView t2=new TextView(context);
        TextView t3=new TextView(context);
        t1.setTextColor(Color.WHITE);
        t2.setTextColor(Color.WHITE);
        t3.setTextColor(Color.WHITE);
        t1.setText(text1);
        t2.setText(text2);
        t2.setGravity(Gravity.CENTER);
        t3.setText(text3);
        tableRow.addView(t1);
        tableRow.addView(t2);
        tableRow.addView(t3);
        int padding=15;
        tableRow.setPadding(padding+20,padding,10,padding);
        TableRow.LayoutParams params=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        t1.setLayoutParams(params);
        t2.setLayoutParams(params);
        t3.setLayoutParams(params);
        infoTable.addView(tableRow);
        if(k==-1)return;
        concentrationTextViews[k]=t2;
        changeTextViews[k]=t3;
    }


    //pretraga polena
    public void checkUserInput(){
        if(selectedLocation==-1){
            MainActivity.debug.print("nije selektovan grad");
            showMessageSnackBar("Select city");
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
                showMessageSnackBar("Wrong date");
                return;
            }
        }catch (Exception e){
            MainActivity.debug.print("nisu unet pravilan datum");
            showMessageSnackBar("Enter date first");
            return;
        }
        Date date=new Date(year,month,day);
        date.setTime(date.getTime()+6*24*60*60*1000);
//        MainActivity.debug.print(date.getYear()+" "+date.getMonth()+" "+date.getDate());
        String start=year+"-"+month+"-"+day;
        String end=date.getYear()+"-"+date.getMonth()+"-"+date.getDate();
        getPollenIdAtLocationAndDate(locationId,start,end);
//        getPollenIdsAtLocationAndInterval(locationId,start,end);
    }
    private void getPollenIdAtLocationAndDate(final int locationId, final String start, final String end){
        String url = "http://polen.sepa.gov.rs/api/opendata/pollens/";
        url+="?location=";
        url+=locationId;
        url=url+"&date="+start;
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
                        getConcentrationAtPollenId(id,locationId,start,end);
                    }else{
                        showMessageSnackBar("No data on that day :(");
                        MainActivity.debug.print("nema nijednog polena tog dana");
                    }
                }catch (Exception e){
//                    showSearchSnackBar();
                    MainActivity.debug.print(e.getMessage());
                    MainActivity.debug.print("Error parsing json getPollenAtLocationAndDate");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSearchSnackBar();
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error getPollenAtLocationAndDate");
            }
        });
        requestQueue.add(request);
    }
    private void getConcentrationAtPollenId(int pollenId, final int locationId, final String start, final String end){
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
                    getPollenIdsAtLocationAndInterval(locationId,start,end);
                }catch (Exception e){
                    MainActivity.debug.print(e.getMessage());
                    MainActivity.debug.print("Error parsing json getConcentrationsAtId");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSearchSnackBar();
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
                changeTextViews[allergens[i].textViewId].setText("no data");
                break;
            }
        }
    }
    private void updateConcentrationChange(int value,int allergenId){
        for(int i=0;i<allergens.length;i++){
            if(allergens[i].id==allergenId){
                int textViewId=allergens[i].textViewId;
                int v=Integer.parseInt(concentrationTextViews[textViewId].getText().toString());
                if(value>v)changeTextViews[textViewId].setText("raste");
                if(value==v)changeTextViews[textViewId].setText("miruje");
                if(value<v)changeTextViews[textViewId].setText("opada");
                break;
            }
        }
    }
    //petraga za interval
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
                showSearchSnackBar();
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
                try {
                    JSONArray array = response.getJSONArray("results");
                    int p=0,n=0;
                    for(int i=0;i<array.length();i++){
                        p+=array.getJSONObject(i).getInt("value");
                        n++;
                        int a1=array.getJSONObject(i).getInt("allergen");
                        if(i+1>=array.length()){
                            updateConcentrationChange(p/n,a1);
                            break;
                        }
                        int a2=array.getJSONObject(i+1).getInt("allergen");
                        if(a1!=a2){
                            updateConcentrationChange(p/n,a1);
                            p=0;
                            n=0;
                        }
                    }
                }catch (Exception e){
                    MainActivity.debug.print("Error parsing json getConcentrationsAtPollenIds");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSearchSnackBar();
                MainActivity.debug.print(error.getMessage());
                MainActivity.debug.print("Error getConcentrationsAtPollenIds");
            }
        });
        requestQueue.add(request);
    }

    //reload
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
//                showReloadSnackBar();
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
                    changeTextViews=new TextView[response.length()];
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
                showReloadSnackBar();
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
                showReloadSnackBar();
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
