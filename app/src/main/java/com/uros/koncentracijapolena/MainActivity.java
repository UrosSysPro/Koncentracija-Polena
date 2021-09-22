package com.uros.koncentracijapolena;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainLayout;
    private LinearLayout navView;
    private RelativeLayout contentView;
    private Button mainBtn,infoBtn,aboutBtn;
    private HomePage homePage;
    public static DebugPage debug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout=new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        contentView=new RelativeLayout(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,0,1);
        contentView.setLayoutParams(params);

        navView=new LinearLayout(this);
        navView.setOrientation(LinearLayout.HORIZONTAL);
        params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150);
        navView.setLayoutParams(params);

        mainLayout.addView(contentView);
        mainLayout.addView(navView);

        createNavBar();
        createPages();

        setContentView(mainLayout);
    }

    private void createNavBar(){
        mainBtn=new Button(this);
        infoBtn=new Button(this);
        aboutBtn=new Button(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);

        mainBtn.setLayoutParams(params);
        infoBtn.setLayoutParams(params);
        aboutBtn.setLayoutParams(params);

        mainBtn.setText("Home");
//        mainBtn.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.ic_launcher_foreground),null,null);
        infoBtn.setText("Info");
        aboutBtn.setText("About");

        mainBtn.setBackgroundColor(Color.DKGRAY);
        mainBtn.setTextColor(Color.WHITE);

        navView.addView(mainBtn);
        navView.addView(infoBtn);
        navView.addView(aboutBtn);
        navView.setBackgroundColor(Color.DKGRAY);

        Window window=getWindow();
        window.setNavigationBarDividerColor(Color.DKGRAY);
        window.setNavigationBarColor(Color.DKGRAY);
        getSupportActionBar().hide();
    }

    public void createPages(){
        debug=new DebugPage(this);
        contentView.addView(debug.getView());
        homePage=new HomePage(this);
        contentView.addView(homePage.getView());
        debug.getView().setZ(1);
    }
}
