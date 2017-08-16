package com.eaglebk.sepro.testtcpmoxy;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


public class App extends Application {

    private static TCPService tcpService;
//    TCPService tcpService;
    boolean isBound = false;

    private static App instance;
    private static Context mContext;
    private Context appContext;

    public void init(Context context){
        if(appContext == null){
            appContext = context;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        startService(new Intent(this, TCPService.class));
        System.out.println("Start application");
        Intent intent = new Intent(this,TCPService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Context getContext() {
        return this;
    }

    public static Context get(){
        return getInstance().getContext();
    }

//    private static App instance;

    public static App getInstance(){
        return instance;
//        return instance == null ?
//                (instance = new App()):
//                instance;
    }

    public static TCPService getInstanceServiceTCP() {
        return tcpService;
    }

    public static void setContext(Context mContext) {
        App.mContext = mContext;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TCPService.LocalBinder binder = (TCPService.LocalBinder)service;
            tcpService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

}
