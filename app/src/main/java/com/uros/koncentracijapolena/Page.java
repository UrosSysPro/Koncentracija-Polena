package com.uros.koncentracijapolena;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Page {
    protected final Context context;
    protected LinearLayout view;
    public Page(Context context){
        this.context=context;
        view=new LinearLayout(context);

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
    }

    public LinearLayout getView() {
        return view;
    }
}
