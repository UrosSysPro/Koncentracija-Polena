package com.uros.koncentracijapolena;

import android.graphics.Color;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainLayout;
    private LinearLayout navView;
    private RelativeLayout contentView;
    private Button mainBtn,infoBtn,aboutBtn;
    private Button[] navBtns;
    public static DebugPage debug;
    private Page[] pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout=new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        contentView=new RelativeLayout(this);
        int darkGray=ResourcesCompat.getColor(getResources(),R.color.DarkGray,null);
        contentView.setBackgroundColor(darkGray);
        getWindow().setStatusBarColor(darkGray);
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
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
        navBtns=new Button[4];
        for(int i=0;i<navBtns.length;i++){
                navBtns[i]=new Button(this);
                navBtns[i].setLayoutParams(params);
                navBtns[i].setBackgroundColor(Color.TRANSPARENT);
                navBtns[i].setShadowLayer(0,0,0,Color.TRANSPARENT);
                navBtns[i].setTextColor(Color.WHITE);
                navView.addView(navBtns[i]);
        }
        mainBtn=navBtns[0];
        mainBtn.setText("Home");
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(0);
            }
        });
//        mainBtn.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.ic_launcher_foreground),null,null);
        infoBtn=navBtns[1];
        infoBtn.setText("Info");
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(1);
            }
        });
        aboutBtn=navBtns[2];
        aboutBtn.setText("About");
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(2);
            }
        });
        navBtns[3].setText("debug");
        navBtns[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(3);
            }
        });
        int lightGray= ResourcesCompat.getColor(getResources(),R.color.LightGray,null);
        navView.setBackgroundColor(lightGray);
        Window window=getWindow();
        window.setNavigationBarDividerColor(lightGray);
        window.setNavigationBarColor(lightGray);
        getSupportActionBar().hide();
    }

    public void createPages(){
        pages=new Page[4];
        pages[0]=new HomePage(this);
        pages[1]=new InfoPage(this);
        pages[2]=new AboutPage(this);
        pages[3]=new DebugPage(this);
        debug=(DebugPage) pages[3];
        for(int i=0;i<pages.length;i++){
            contentView.addView(pages[i].getView());
        }
        showPage(0);
    }
    public void showPage(int index){
        for(int i=0;i<pages.length;i++){
            if(i==index){
                pages[i].getView().setVisibility(View.VISIBLE);
                continue;
            }
            pages[i].getView().setVisibility(View.INVISIBLE);
        }
    }
}
