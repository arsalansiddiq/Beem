package com.example.arsalansiddiq.beem.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.azimolabs.maskformatter.MaskFormatter;
import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.requestmodels.ba.BAAddFeedbackRequestModel;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class FeedbackBAActivity extends BaseActivity {

    @BindView(R.id.edtText_customerName)
    EditText edtText_customerName;

    @BindView(R.id.edtText_contactNumb)
    EditText edtText_contactNumb;

    @BindView(R.id.ratingBar_submitFeedbackBA)
    RatingBar ratingBar_submitFeedbackBA;

    @BindView(R.id.edtText_productName)
    EditText edtText_productName;

    @BindView(R.id.edtText_comments)
    EditText edtText_comments;

    @BindView(R.id.btn_submitBAFeedback)
    Button btn_submitBAFeedback;

    String customerName, number, productName, comments;
    int ratingUser;

    private NetworkUtils networkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_b);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        initProgressBar();

        networkUtils = new NetworkUtils(this);

        setupEditTextWithDashes(R.id.edtText_contactNumb, Constants.NUMBERS_DASHED_MASK);

        ratingBar_submitFeedbackBA.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingUser = (int) v;
            }
        });

        btn_submitBAFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtText_contactNumb.setTextColor(Color.parseColor("#000000"));
                customerName = edtText_customerName.getText().toString();
                number = edtText_contactNumb.getText().toString();
                productName = edtText_productName.getText().toString();
                comments = edtText_comments.getText().toString();

                if (TextUtils.isEmpty(customerName) || TextUtils.isEmpty(number) ||
                        TextUtils.isEmpty(productName) || TextUtils.isEmpty(comments)) {
                    Toast.makeText(FeedbackBAActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    if (number.length() != 13 || ratingUser < 1) {
                        if (number.length() != 13) {
                            edtText_contactNumb.setTextColor(Color.parseColor("#ff0000"));
                        }
                        if (ratingUser < 1) {
                            Toast.makeText(FeedbackBAActivity.this, "please rate with stars", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        if (networkUtils.isNetworkConnected()) {
                            progressShow();

                            BAAddFeedbackRequestModel baAddFeedbackRequestModel =
                                    new BAAddFeedbackRequestModel(customerName, number, ratingUser, productName, comments);

                            networkUtils.addFeedbackBA(baAddFeedbackRequestModel, new BaseCallbackInterface() {
                                @Override
                                public void success(Response response) {
                                    progressHide();
                                    finish();
                                }

                                @Override
                                public void failure(String error) {
                                    progressHide();
                                    Toast.makeText(FeedbackBAActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(FeedbackBAActivity.this, "no internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    //Number Validation
    private void setupEditTextWithDashes(int layoutId, String mask) {
        EditText field = (EditText) findViewById(layoutId);
        field.addTextChangedListener(new MaskFormatter(mask, field));
    }
}
