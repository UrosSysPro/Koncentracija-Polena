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

public class InfoPage extends Page {
    public InfoPage(Context context){
        super(context);
        view.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView=new ImageView(context);
        Drawable d=ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_launcher_foreground,null);
        imageView.setImageDrawable(d);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setAdjustViewBounds(true);

        TextView textView=new TextView(context);
        textView.setText("Koncentracija Polena u Vazduhu");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0,0,0,20);
        textView.setTextSize(20);


        TextView desc=new TextView(context);
        desc.setText(R.string.app_description);
        desc.setGravity(Gravity.CENTER);
        desc.setTextColor(Color.WHITE);
        desc.setPadding(30,0,30,0);
        desc.setMovementMethod(LinkMovementMethod.getInstance());

        view.addView(imageView);
        view.addView(textView);
        view.addView(desc);
    }
}
