package com.uros.koncentracijapolena;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class Page {
    protected final Context context;
    protected LinearLayout view;
    protected ScrollView scrollView;
    public Page(Context context){
        this.context=context;
        view=new LinearLayout(context);
        view.setPadding(25,15,25,15);
        scrollView=new ScrollView(context);

        scrollView.addView(view);

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(params);
    }

    public ScrollView getView() {
        return scrollView;
    }
}
