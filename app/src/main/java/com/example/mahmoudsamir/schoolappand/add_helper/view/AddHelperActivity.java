package com.example.mahmoudsamir.schoolappand.add_helper.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.add_helper.presenter.AddHelperInteractor;
import com.example.mahmoudsamir.schoolappand.add_helper.presenter.AddHelperPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddHelperActivity extends AppCompatActivity implements AddHelperView {

    AddHelperPresenter presenter;
    @BindView(R.id.phone_edx)
    EditText phone_edx;

    @BindView(R.id.add_helper)
    Button add_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_helper);
        ButterKnife.bind(this);

        presenter = new AddHelperPresenter(this, new AddHelperInteractor());

        add_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHelper();
            }
        });
    }

    private void addHelper() {
        presenter.addHelper(phone_edx.getText().toString());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorAddingHelper() {

    }

    @Override
    public void onSuccessAddingHelper() {

    }

}
