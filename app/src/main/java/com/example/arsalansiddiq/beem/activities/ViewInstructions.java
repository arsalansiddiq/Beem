package com.example.arsalansiddiq.beem.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.viewinstruction.ViewInstructionResponseModel;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ViewInstructions extends BaseActivity implements UpdateCallback{

//    @BindView(R.id.constraintLayout_viewInstuction)
//    ConstraintLayout constraintLayout_viewInstuction;

    @BindView(R.id.imgView_viewInstruction)
    ImageView imgView_viewInstruction;

    private NetworkUtils networkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_instructions);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        initProgressBar();

        networkUtils = new NetworkUtils(this);

        progressShow();
        networkUtils.getViewInstruction(38,
                5, this);

    }

    @Override
    public void UpdateSuccess(Response response) {
        ViewInstructionResponseModel viewInstructionResponseModel = (ViewInstructionResponseModel) response.body();

        Picasso.get().load(viewInstructionResponseModel.getData().getImagePath()).into(imgView_viewInstruction);

        progressHide();
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        progressHide();
    }
}
