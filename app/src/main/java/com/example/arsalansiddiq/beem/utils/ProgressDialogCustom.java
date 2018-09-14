package com.example.arsalansiddiq.beem.utils;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by jellani on 9/8/2018.
 */

public class ProgressDialogCustom {

    private Context context;
    private ProgressBar progressBar;

    public ProgressDialogCustom(Context context) {
        this.context = context;
//        this.progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
//        progressBar.setIndeterminate(true);
//
//        RelativeLayout relativeLayout = new RelativeLayout(context);
//        relativeLayout.setGravity(Gravity.CENTER);
//        relativeLayout.addView(progressBar);
//
//
//
//        RelativeLayout.LayoutParams params = new
//                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        progressBar.setVisibility(View.INVISIBLE);
////        progressBar.set
//
//        Activity activity = (Activity) context;
//        activity.addContentView(relativeLayout, params);

    }


    public void createProgressBar() {
        if (progressBar != null && progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void showProgress() {
        if (progressBar != null && progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
