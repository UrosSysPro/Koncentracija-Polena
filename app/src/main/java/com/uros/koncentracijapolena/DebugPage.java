package com.uros.koncentracijapolena;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DebugPage extends Page{
    public DebugPage(Context context){
        super(context);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setBackgroundColor(Color.LTGRAY);
    }
    public void print(String s){
        TextView textView=new TextView(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setText(s);
        view.addView(textView);
    }
}
