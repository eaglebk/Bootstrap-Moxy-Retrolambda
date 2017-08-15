package com.eaglebk.sepro.testtcpmoxy.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.eaglebk.sepro.testtcpmoxy.R;
import com.eaglebk.sepro.testtcpmoxy.presentation.presenter.MainPresenter;
import com.eaglebk.sepro.testtcpmoxy.presentation.view.MainView;

public class MainActivity extends MvpActivity implements MainView {
    public static final String TAG = "MainActivity";
    @InjectPresenter
    MainPresenter mMainPresenter;
    Button btn;
    RelativeLayout relativeLayout;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        btn.setOnClickListener(v->changeBackground());
    }

    private void changeBackground() {
        relativeLayout.setBackgroundColor(Color.BLUE);
    }
}
