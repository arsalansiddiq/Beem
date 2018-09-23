package com.example.arsalansiddiq.beem.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;


public class BaseActivity  extends AppCompatActivity {


    protected ProgressBar progressBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initProgressBar();

    }


    protected void initProgressBar() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

//        progressBar = new ProgressBar(this);
//
//        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
//        progressBar.setIndeterminate(true);
//
//        RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
//        relativeLayout.setGravity(Gravity.CENTER);
//        relativeLayout.addView(progressBar);
//
//        RelativeLayout.LayoutParams params = new
//                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        progressBar.setVisibility(View.INVISIBLE);
//
//        this.addContentView(relativeLayout, params);
    }


    protected void progressShow() {
        progressDialog.show();
//        if (progressBar != null && progressBar.getVisibility() == View.INVISIBLE) {
//            progressBar.setVisibility(View.VISIBLE);
//        }
    }


    protected void progressHide() {
        progressDialog.dismiss();
//        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
//            progressBar.setVisibility(View.INVISIBLE);
//            progressBar = null;
//        }
    }

}
