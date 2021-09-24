package com.uros.koncentracijapolena;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;

public class AboutPage extends Page {
    public AboutPage(Context context){
        super(context);
//        view.setBackgroundColor(Color.RED);
        view.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView=new ImageView(context);
        Drawable d=ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_launcher_foreground,null);
        imageView.setImageDrawable(d);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setAdjustViewBounds(true);

        TextView[] textViews=new TextView[5];

        for(int i=0;i<textViews.length;i++){
            textViews[i]=new TextView(context);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setPadding(30,0,30,0);
            textViews[i].setTextColor(Color.WHITE);
        }
        textViews[0].setText("Autor: Uros Karaleic");
        textViews[1].setText(R.string.github);
        textViews[1].setMovementMethod(LinkMovementMethod.getInstance());

        textViews[2].setText("Biblioteke koje se koriste u projektu:");

        textViews[3].setText(R.string.volley);
        textViews[3].setMovementMethod(LinkMovementMethod.getInstance());

        textViews[4].setText(R.string.materialSpinner);
        textViews[4].setMovementMethod(LinkMovementMethod.getInstance());

        view.addView(imageView);
        for(int i=0;i<textViews.length;i++){
            view.addView(textViews[i]);
        }
    }
}
