package com.eaglebk.sepro.testtcpmoxy.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.eaglebk.sepro.testtcpmoxy.R;
import com.eaglebk.sepro.testtcpmoxy.presentation.presenter.MainPresenter;
import com.eaglebk.sepro.testtcpmoxy.presentation.view.MainView;

import java.util.Random;

public class MainActivity extends MvpActivity implements MainView {
    public static final String TAG = "MainActivity";
    @InjectPresenter
    MainPresenter mMainPresenter;
    Button btn;
    RelativeLayout relativeLayout;
    BroadcastReceiver br;

    public final static String PARAM_STATUS = "status";
    public final static int STATUS_START = 100;
    public final static String BROADCAST_ACTION = "eagle.testtcpmoxy";

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

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra(PARAM_STATUS);
                btn.setText(status);
            }
        };

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);



//        Intent intent = new Intent(this,TCPService.class);
//        startService(intent.putExtra("time", 3).putExtra("label", "Call 1") );
    }

    private void changeBackground() {
        Random rand = new Random();

        int red = rand.nextInt(256);
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);

        int randColor = Color.rgb(red, green, blue);

        relativeLayout.setBackgroundColor(randColor);
        mMainPresenter.onClickedShowMessage(randColor);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,"Cliked!", Toast.LENGTH_SHORT).show();
    }
}
